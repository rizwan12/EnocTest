package com.example.enoctest.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.enoctest.R
import com.example.enoctest.common.Status
import com.example.enoctest.data.backend.impl.ApiHelper
import com.example.enoctest.data.backend.impl.RetrofitFactory
import com.example.enoctest.factory.LoginViewModelFactory
import com.example.enoctest.ui.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var emailAddress: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Create View Model from Factory
        viewModel = ViewModelProviders.of(
            this,
            LoginViewModelFactory(ApiHelper(RetrofitFactory.apiService))
        ).get(LoginViewModel::class.java)

        // Check for user already logged in
        checkForUser()

        emailAddress = findViewById(R.id.email_address)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)

        loginButton.setOnClickListener(View.OnClickListener {
            val email = emailAddress.text.toString()
            val password = password.text.toString()

            if (email.length <= 0) {
                Toast.makeText(applicationContext, "Please enter email address", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (password.length <= 0) {
                Toast.makeText(applicationContext, "Please enter password", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            login(email, password)
        })
    }

    fun checkForUser() {
        if (viewModel.checkToken(applicationContext)) {
            val sharedPreferences: SharedPreferences = this.getSharedPreferences("EnocTest", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", "")
            val userId = sharedPreferences.getString("userId", "")

            if (token != null && userId != null) {
                openProfileActivity(token, userId)
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModel.login(email, password)
            .observe(this, Observer {
                it?.let {   resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { response ->
                                val token = response.body()?.token
                                val userId = response.body()?.userId
                                if (token != null && userId != null) {
                                    viewModel.saveToken(applicationContext, token, userId)
                                    openProfileActivity(token, userId)
                                }
                            }
                        }
                        Status.ERROR -> {
                            Toast.makeText(applicationContext, "Error occured", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }

    private fun openProfileActivity(token: String, userId: String) {
        val intent: Intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("token", token)
        intent.putExtra("userId", userId)
        startActivity(intent)
        finish()
    }
}