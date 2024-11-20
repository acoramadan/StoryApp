package com.muflidevs.storyapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.muflidevs.storyapp.data.remote.repository.AuthRepository
import com.muflidevs.storyapp.data.remote.retrofit.ApiConfig
import com.muflidevs.storyapp.data.remote.retrofit.ApiService
import com.muflidevs.storyapp.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var authRepo: AuthRepository
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiService = ApiConfig.getApiService()
        authRepo = AuthRepository(apiService,this)
        val token = authRepo.getToken()
        Log.e("SplashScreen","$token")
        Handler(Looper.getMainLooper()).postDelayed({
            if(token != null) {
                val intent = Intent(this@SplashScreenActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashScreenActivity,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        },5000)
    }
}