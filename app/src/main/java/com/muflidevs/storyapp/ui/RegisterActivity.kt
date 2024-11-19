package com.muflidevs.storyapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.muflidevs.storyapp.R
import com.muflidevs.storyapp.data.remote.response.RegisterRequest
import com.muflidevs.storyapp.data.remote.retrofit.ApiConfig
import com.muflidevs.storyapp.data.remote.retrofit.ApiService
import com.muflidevs.storyapp.databinding.ActivityRegisterBinding
import com.muflidevs.storyapp.helper.HelperAnimation
import com.muflidevs.storyapp.helper.HelperCustomView
import com.muflidevs.storyapp.ui.customView.CustomButton
import com.muflidevs.storyapp.ui.customView.CustomEmailEditText
import com.muflidevs.storyapp.ui.customView.CustomPasswordEditText
import com.muflidevs.storyapp.ui.customView.CustomUsernameEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var apiService: ApiService
    private lateinit var loginTv: TextView
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
        apiService = ApiConfig.getApiService()
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
            register(name,email,password)
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

    @SuppressLint("ShowToast")
    private fun register(name: String, email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.postRegister(RegisterRequest(name,email,password))
                if(response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity,"${response.body()!!.message}",Toast.LENGTH_SHORT)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity,"Error: ${response.body()!!.message}",Toast.LENGTH_SHORT)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("REGISTER ACTIVITY","${e.message}")
                }
            }
        }
    }

}