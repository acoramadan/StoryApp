package com.muflidevs.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.muflidevs.storyapp.R
import com.muflidevs.storyapp.databinding.ActivityLoginBinding
import com.muflidevs.storyapp.helper.HelperAnimation
import com.muflidevs.storyapp.helper.HelperCustomView
import com.muflidevs.storyapp.ui.customView.CustomButton
import com.muflidevs.storyapp.ui.customView.CustomEmailEditText
import com.muflidevs.storyapp.ui.customView.CustomPasswordEditText

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var registerTv: TextView
    private lateinit var emailEdtTxt: CustomEmailEditText
    private lateinit var passwordEdtTxt: CustomPasswordEditText
    private lateinit var submitBtn: CustomButton
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerTv = binding.register
        emailEdtTxt = binding.edLoginEmail
        passwordEdtTxt = binding.edLoginPassword
        submitBtn = binding.loginBtn

        //navigation
        registerTv.setOnClickListener(this)

        HelperCustomView.setMyButtonEnabled(emailEdtTxt,passwordEdtTxt,submitBtn)
        HelperCustomView.checkUserInput(emailEdtTxt,passwordEdtTxt,submitBtn)
        HelperAnimation.playAnimation(emailEdtTxt,passwordEdtTxt,submitBtn,registerTv)

    }
    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.register -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.login_btn -> {

            }
        }
    }


}