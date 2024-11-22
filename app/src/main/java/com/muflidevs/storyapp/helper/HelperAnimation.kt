package com.muflidevs.storyapp.helper

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.widget.TextView
import com.muflidevs.storyapp.ui.customView.CustomButton
import com.muflidevs.storyapp.ui.customView.CustomEmailEditText
import com.muflidevs.storyapp.ui.customView.CustomPasswordEditText
import com.muflidevs.storyapp.ui.customView.CustomUsernameEditText

object HelperAnimation {

    fun playAnimation(
        emailEdtTxt: CustomEmailEditText,
        passwordEdtTxt: CustomPasswordEditText,
        submitBtn: CustomButton,
        loginEdtTxt: TextView
    ) {

        val email = ObjectAnimator.ofFloat(emailEdtTxt, View.ALPHA, 1f).setDuration(200)
        val password = ObjectAnimator.ofFloat(passwordEdtTxt, View.ALPHA, 1f).setDuration(200)
        val button = ObjectAnimator.ofFloat(submitBtn, View.ALPHA, 1f).setDuration(200)
        val loginText = ObjectAnimator.ofFloat(loginEdtTxt, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(email, password, button, loginText)
            start()
        }
    }

    fun playAnimation(
        userNameEdtText: CustomUsernameEditText,
        emailEdtTxt: CustomEmailEditText,
        passwordEdtTxt: CustomPasswordEditText,
        submitBtn: CustomButton,
        loginEdtTxt: TextView
    ) {
        val userName = ObjectAnimator.ofFloat(userNameEdtText, View.ALPHA, 1f).setDuration(200)
        val email = ObjectAnimator.ofFloat(emailEdtTxt, View.ALPHA, 1f).setDuration(200)
        val password = ObjectAnimator.ofFloat(passwordEdtTxt, View.ALPHA, 1f).setDuration(200)
        val button = ObjectAnimator.ofFloat(submitBtn, View.ALPHA, 1f).setDuration(200)
        val loginText = ObjectAnimator.ofFloat(loginEdtTxt, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(userName, email, password, button, loginText)
            start()
        }
    }
}