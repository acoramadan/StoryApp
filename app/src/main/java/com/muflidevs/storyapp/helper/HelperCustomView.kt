package com.muflidevs.storyapp.helper

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.muflidevs.storyapp.ui.customView.CustomButton
import com.muflidevs.storyapp.ui.customView.CustomEmailEditText
import com.muflidevs.storyapp.ui.customView.CustomPasswordEditText
import com.muflidevs.storyapp.ui.customView.CustomUsernameEditText

object HelperCustomView {
    fun isValidEmail(target: CharSequence?): Boolean {
        if(TextUtils.isEmpty(target)) return false
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target!!).matches()
    }

    fun setMyButtonEnabled(userNameEdtTxt: CustomUsernameEditText,
                           emailEdtTxt: CustomEmailEditText,
                           passwordEdtTxt: CustomPasswordEditText,
                           submitBtn: CustomButton) {
        val resultEmail = emailEdtTxt.text
        val resultPassword = passwordEdtTxt.text
        val resultUsername = userNameEdtTxt.text
        val isEmailNotEmpty = resultEmail != null && emailEdtTxt.toString().isNotEmpty()
        val isPasswordNotEmpty = resultPassword != null &&  resultPassword.length < 8 && passwordEdtTxt.toString().isNotEmpty()
        val isUsernameNotEmpty = resultUsername != null && userNameEdtTxt.toString().isNotEmpty()
        submitBtn.isEnabled = !(isEmailNotEmpty && isPasswordNotEmpty && isUsernameNotEmpty)  && isValidEmail(resultEmail.toString())
    }

    fun setMyButtonEnabled(emailEdtTxt: CustomEmailEditText,
                           passwordEdtTxt: CustomPasswordEditText,
                           submitBtn: CustomButton) {
        val resultEmail = emailEdtTxt.text
        val resultPassword = passwordEdtTxt.text
        val isEmailNotEmpty = resultEmail != null && emailEdtTxt.toString().isNotEmpty()
        val isPasswordNotEmpty = resultPassword != null &&  resultPassword.length < 8 && passwordEdtTxt.toString().isNotEmpty()
        submitBtn.isEnabled = !(isEmailNotEmpty && isPasswordNotEmpty) && isValidEmail(resultEmail.toString())
    }

    // method ini untuk halaman register
    fun checkUserInput (userNameEdtTxt: CustomUsernameEditText,
                        emailEdtTxt: CustomEmailEditText,
                        passwordEdtTxt: CustomPasswordEditText,
                        submitBtn: CustomButton) {
        emailEdtTxt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnabled(userNameEdtTxt,emailEdtTxt,passwordEdtTxt,submitBtn)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        passwordEdtTxt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnabled(userNameEdtTxt,emailEdtTxt,passwordEdtTxt,submitBtn)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        userNameEdtTxt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnabled(userNameEdtTxt,emailEdtTxt,passwordEdtTxt,submitBtn)
            }
            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    // method ini untuk halaman login
    fun checkUserInput (emailEdtTxt: CustomEmailEditText,
                        passwordEdtTxt: CustomPasswordEditText,
                        submitBtn: CustomButton) {
        emailEdtTxt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnabled(emailEdtTxt,passwordEdtTxt,submitBtn)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        passwordEdtTxt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnabled(emailEdtTxt,passwordEdtTxt,submitBtn)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

}