package com.muflidevs.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.muflidevs.storyapp.R
import com.muflidevs.storyapp.databinding.ActivityLoginBinding
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
        emailEdtTxt = binding.email
        passwordEdtTxt = binding.password
        submitBtn = binding.loginBtn

        //navigation
        registerTv.setOnClickListener(this)

        setMyButtonEnabled()
        checkUserInput()

    }

    private fun setMyButtonEnabled() {
        val resultEmail = emailEdtTxt.text
        val resultPassword = passwordEdtTxt.text
        val isEmailNotEmpty = resultEmail != null && emailEdtTxt.toString().isNotEmpty()
        val isPasswordNotEmpty = resultPassword != null &&  resultPassword.length < 8 && passwordEdtTxt.toString().isNotEmpty()
        submitBtn.isEnabled = !(isEmailNotEmpty && isPasswordNotEmpty)

    }
    private fun checkUserInput() {
        emailEdtTxt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnabled()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        passwordEdtTxt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnabled()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
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