package com.example.enoctest.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.security.MessageDigest

object ImageUtils {

    public fun applySapiaFilter(src: Bitmap, depth: Int, red: Double, green: Double, blue: Double): Bitmap? {
        // image size
        val width = src.width
        val height = src.height
        // create output bitmap
        val bmOut = Bitmap.createBitmap(width, height, src.config)
        // constant grayscale
        val GS_RED = 0.3
        val GS_GREEN = 0.59
        val GS_BLUE = 0.11
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // scan through all pixels
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get pixel color
                pixel = src.getPixel(x, y)
                // get color on each channel
                A = Color.alpha(pixel)
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)
                // apply grayscale sample
                R = (GS_RED * R + GS_GREEN * G + GS_BLUE * B).toInt()
                G = R
                B = G

                // apply intensity level for sepid-toning on each channel
                R += (depth * red).toInt()
                if (R > 255) {
                    R = 255
                }
                G += (depth * green).toInt()
                if (G > 255) {
                    G = 255
                }
                B += (depth * blue).toInt()
                if (B > 255) {
                    B = 255
                }

                // set new pixel color to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        // return final image
        return bmOut
    }

    public fun Uri.getCapturedImage(context: Context): Bitmap? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source =
                context?.contentResolver?.let { ImageDecoder.createSource(it, this) }
            return source?.let { ImageDecoder.decodeBitmap(it) }
        } else {
            return MediaStore.Images.Media.getBitmap(
                context?.contentResolver,
                this
            )
        }
    }

    public fun sizeInMb(data: Bitmap): Int {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            (data.byteCount/(1024*1024))
        } else {
            (data.allocationByteCount/(1024*1024))
        }
    }

    public fun getBase64FromBitmap(image: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()

        val convertImage: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
        return convertImage
    }

    public fun getEmailHash(email: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(email.trim().toByteArray())).toString(16).padStart(32, '0')
    }
}