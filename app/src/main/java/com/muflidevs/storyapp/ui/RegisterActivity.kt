package com.muflidevs.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.muflidevs.storyapp.R
import com.muflidevs.storyapp.data.remote.repository.AuthRepository
import com.muflidevs.storyapp.data.remote.retrofit.ApiConfig
import com.muflidevs.storyapp.databinding.ActivityRegisterBinding
import com.muflidevs.storyapp.helper.HelperAnimation
import com.muflidevs.storyapp.helper.HelperCustomView
import com.muflidevs.storyapp.helper.HelperCustomView.showToast
import com.muflidevs.storyapp.ui.customView.CustomButton
import com.muflidevs.storyapp.ui.customView.CustomEmailEditText
import com.muflidevs.storyapp.ui.customView.CustomPasswordEditText
import com.muflidevs.storyapp.ui.customView.CustomUsernameEditText
import com.muflidevs.storyapp.viewModel.AuthViewModel

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var viewModel: AuthViewModel
    private lateinit var loginTv: TextView
    private lateinit var loadingBar: ProgressBar
    private lateinit var userNameEdtTxt: CustomUsernameEditText
    private lateinit var emailEdtTxt: CustomEmailEditText
    private lateinit var passwordEdtTxt: CustomPasswordEditText
    private lateinit var submitBtn: CustomButton
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //api
        viewModel = AuthViewModel(AuthRepository(ApiConfig.getApiService(),this))
        loadingBar = binding.progressBar
        loginTv = binding.loginTv
        userNameEdtTxt = binding.edRegisterName
        emailEdtTxt = binding.edRegisterEmail
        passwordEdtTxt = binding.edRegisterPassword
        submitBtn = binding.submitBtnRegister

        //navigation
        loginTv.setOnClickListener(this)

        submitBtn.setOnClickListener {
            val name = userNameEdtTxt.text.toString()
            val email = emailEdtTxt.text.toString()
            val password = passwordEdtTxt.text.toString()
            viewModel.register(name,email,password)
            viewModel.isLoading.observe(this) {
                showLoading(it)
            }
            viewModel.registerResult.observe(this) { registerResult ->
                if(!registerResult.error!!) {
                    showToast(this@RegisterActivity,"Registrasi Berhasil Silahkan Login")
                    val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showToast(this@RegisterActivity, registerResult.message)
                }
            }
        }

        HelperCustomView.setMyButtonEnabled(userNameEdtTxt,emailEdtTxt,passwordEdtTxt,submitBtn)
        HelperCustomView.checkUserInput(userNameEdtTxt,emailEdtTxt,passwordEdtTxt,submitBtn)
        HelperAnimation.playAnimation(userNameEdtTxt,emailEdtTxt,passwordEdtTxt,submitBtn,loginTv)
    }


    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.login_tv -> {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    private fun showLoading(loading: Boolean) {
        loadingBar.visibility = if(loading) View.VISIBLE else View.GONE
    }
}