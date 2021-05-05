package com.example.enoctest.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.enoctest.R
import com.example.enoctest.common.ImageUtils
import com.example.enoctest.common.ImageUtils.getCapturedImage
import com.example.enoctest.common.Status
import com.example.enoctest.data.backend.impl.ApiHelper
import com.example.enoctest.data.backend.impl.RetrofitFactory
import com.example.enoctest.factory.ProfileViewModelFactory
import com.example.enoctest.ui.viewmodel.ProfileViewModel


class ProfileActivity : AppCompatActivity() {

    // companion object
    companion object {
        private val IMAGE_CHOOSE = 1000;
        private val PERMISSION_CODE = 1001;
    }

    private lateinit var profileImageButton: ImageButton
    private lateinit var logoutButton: Button
    private lateinit var emailTextView: TextView
    private lateinit var passwordTextView: TextView

    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initializing view model
        viewModel = ViewModelProviders.of(
            this,
            ProfileViewModelFactory(ApiHelper(RetrofitFactory.apiService))
        ).get(ProfileViewModel::class.java)

        val token = intent.getStringExtra("token") ?: ""
        val userId = intent.getStringExtra("userId") ?: ""

        viewModel.token = token
        viewModel.userId = userId

        emailTextView = findViewById(R.id.email_label)
        passwordTextView = findViewById(R.id.password_label)
        logoutButton = findViewById(R.id.logout_button)
        profileImageButton = findViewById(R.id.user_image)
        profileImageButton.setOnClickListener(View.OnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    chooseImageGallery()
                }
            } else {
                chooseImageGallery()
            }
        })

        logoutButton.setOnClickListener(View.OnClickListener {
            val sharedPreferences: SharedPreferences = this.getSharedPreferences(
                "EnocTest",
                Context.MODE_PRIVATE
            )
            val editor = sharedPreferences.edit()
            editor.putString("token", "")
            editor.apply()
            editor.commit()

            openLoginActivity()
        })

        // Load user
        viewModel.getUser()
            .observe(this, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { response ->
                                val email = response.body()?.email
                                val password = response.body()?.password
                                val imageUrl = response.body()?.avatarUrl
                                if (email != null && password != null) {
                                    updateUI(email, password, imageUrl)
                                }

                                // If image is null from backend then load from Gravatar
                                if (email != null && imageUrl == null) {
                                    loadImageFromGravatar(email)
                                }
                            }
                        }
                        Status.ERROR -> {
                            Toast.makeText(applicationContext, "Error occured", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            })
    }

    private fun updateUI(email: String, password: String, imageUrl: String?) {
        emailTextView.text = "Email: " + email
        passwordTextView.text = "Password: " + password

        if (imageUrl != null) {
            loadImageFromUrl(imageUrl)
        }
    }

    private fun loadImageFromUrl(imageUrl: String) {
        Glide.with(applicationContext).load(imageUrl).into(profileImageButton)
    }

    private fun loadImageFromGravatar(email: String) {
        Toast.makeText(this, "Loading image from gravatar.com if available", Toast.LENGTH_SHORT).show()

        val hashString = ImageUtils.getEmailHash(email)
        Glide.with(applicationContext).load("https://www.gravatar.com/avatar/" + hashString).into(profileImageButton)
    }

    private fun openLoginActivity() {
        val intent: Intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun uploadImage(image: Bitmap) {
        val base64Image = ImageUtils.getBase64FromBitmap(image)
        viewModel.uploadImage(base64Image)
            .observe(this, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { response ->
                                val imageUrl = response.body()?.avatarUrl
                                if (imageUrl != null) {
                                    Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        Status.ERROR -> {
                            Toast.makeText(applicationContext, "Error occured", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            })
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK) {

            val uri = data?.data
            var bitmap = uri?.getCapturedImage(applicationContext)

            if (bitmap != null) {
                val size = ImageUtils.sizeInMb(bitmap)
                bitmap = ImageUtils.applySapiaFilter(bitmap, 50, 5.0, 0.0, 5.0)
                profileImageButton.setImageBitmap(bitmap)

                // Check for app size
                if (size > 1) {
                    android.widget.Toast.makeText(
                        this,
                        "Greater than 1 MB",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                } else {
                    android.widget.Toast.makeText(
                        this,
                        "Less than 1 MB, Uploading image . . .",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                    if (bitmap != null) {
                        uploadImage(bitmap)
                    }
                }
            }
        }
    }

    // On permission rejected or accepted
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImageGallery()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}