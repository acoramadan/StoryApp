package com.muflidevs.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.muflidevs.storyapp.R
import com.muflidevs.storyapp.data.remote.repository.AuthRepository
import com.muflidevs.storyapp.data.remote.retrofit.ApiConfig
import com.muflidevs.storyapp.databinding.ActivityLoginBinding
import com.muflidevs.storyapp.helper.HelperAnimation
import com.muflidevs.storyapp.helper.HelperCustomView
import com.muflidevs.storyapp.helper.HelperCustomView.showToast
import com.muflidevs.storyapp.ui.customView.CustomButton
import com.muflidevs.storyapp.ui.customView.CustomEmailEditText
import com.muflidevs.storyapp.ui.customView.CustomPasswordEditText
import com.muflidevs.storyapp.viewModel.AuthViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var viewModel: AuthViewModel
    private lateinit var registerTv: TextView
    private lateinit var emailEdtTxt: CustomEmailEditText
    private lateinit var passwordEdtTxt: CustomPasswordEditText
    private lateinit var submitBtn: CustomButton
    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AuthViewModel(AuthRepository(ApiConfig.getApiService(), this))
        registerTv = binding.register
        emailEdtTxt = binding.edLoginEmail
        passwordEdtTxt = binding.edLoginPassword
        submitBtn = binding.loginBtn
        progressBar = binding.progressBar
        //navigation
        registerTv.setOnClickListener(this)

        submitBtn.setOnClickListener {
            viewModel.login(emailEdtTxt.text.toString(), passwordEdtTxt.text.toString())
            Log.d(
                "LoginActivity",
                "{${emailEdtTxt.text.toString()}} ${passwordEdtTxt.text.toString()}"
            )
            viewModel.isLoading.observe(this) {
                showLoading(it)
            }
            viewModel.loginResult.observe(this) { loginResponse ->
                Log.d("LoginActivity", "${loginResponse.error ?: "null"}")
                if (loginResponse != null && !loginResponse.error!!) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    showToast(
                        this@LoginActivity,
                        "Selamat Datang ${loginResponse.loginResult!!.name ?: "null"}"
                    )
                    startActivity(intent)
                    finish()
                }
                Log.d("LoginActivity", "Error status: ${loginResponse.error}")
            }
            viewModel.error.observe(this) { isError ->
                showToast(this, isError)
            }
        }
        HelperCustomView.setMyButtonEnabled(emailEdtTxt, passwordEdtTxt, submitBtn)
        HelperCustomView.checkUserInput(emailEdtTxt, passwordEdtTxt, submitBtn)
//        HelperAnimation.playAnimation(emailEdtTxt, passwordEdtTxt, submitBtn, registerTv)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.register -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}