package com.muflidevs.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.muflidevs.storyapp.R
import com.muflidevs.storyapp.databinding.ActivityRegisterBinding
import com.muflidevs.storyapp.helper.HelperAnimation
import com.muflidevs.storyapp.helper.HelperCustomView
import com.muflidevs.storyapp.ui.customView.CustomButton
import com.muflidevs.storyapp.ui.customView.CustomEmailEditText
import com.muflidevs.storyapp.ui.customView.CustomPasswordEditText
import com.muflidevs.storyapp.ui.customView.CustomUsernameEditText

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
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
        loginTv = binding.loginTv
        userNameEdtTxt = binding.edRegisterName
        emailEdtTxt = binding.edRegisterEmail
        passwordEdtTxt = binding.edRegisterPassword
        submitBtn = binding.submitBtn

        //navigation
        loginTv.setOnClickListener(this)

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


}