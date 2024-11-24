package com.nguyen.image1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nguyen.image1.databinding.ActivityMainBinding
import java.net.URL

private const val TAG = "MainActivity"
private const val URL = "https://rkpandey.com/images/rkpDavidson.jpg"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d(TAG, "Loading image from URL into ImageView")
        // downloadBitmap() needs to be moved off the main thread. however we can call
        // StrictMode.setThreadPolicy() to allow this on main thread, which is not recommended in
        // production. also the UI won't be responsive.
        val policy = StrictMode.ThreadPolicy.Builder().permitNetwork().build()
        StrictMode.setThreadPolicy(policy)
        val bitmap = downloadBitmap(URL)
        binding.image.setImageBitmap(bitmap)
    }

    private fun downloadBitmap(url: String): Bitmap? {
        return try {
            val connection = URL(url).openConnection()
            connection.connect()
            val stream = connection.getInputStream()
            val bitmap = BitmapFactory.decodeStream(stream)
            stream.close()
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "Exception $e")
            null
        }
    }
}