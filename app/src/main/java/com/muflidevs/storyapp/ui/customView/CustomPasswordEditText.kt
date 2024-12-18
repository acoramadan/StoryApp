package com.muflidevs.storyapp.ui.customView

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.muflidevs.storyapp.R

class CustomPasswordEditText : AppCompatEditText, View.OnTouchListener {
    private var clearButtonImage: Drawable? = null
    private var iconUserImage: Drawable? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.close_icon) as Drawable
        iconUserImage =
            ContextCompat.getDrawable(context, R.drawable.user_login_password_icon) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s.toString().isEmpty()) showUserIcon()
                else if (s.toString().isNotEmpty()) showClearButton()
                else hideClearButton()
                if (s.toString().length < 8) error = "Panjang password kurang dari 8"

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        hint = "Password"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        textSize = 20f
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage!!.intrinsicWidth + paddingStart).toFloat()
                when {
                    event!!.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart =
                    (width - paddingEnd - clearButtonImage!!.intrinsicWidth).toFloat()
                when {
                    event!!.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage =
                            ContextCompat.getDrawable(context, R.drawable.close_icon) as Drawable
                        showClearButton()
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        clearButtonImage =
                            ContextCompat.getDrawable(context, R.drawable.close_icon) as Drawable
                        iconUserImage = ContextCompat.getDrawable(
                            context,
                            R.drawable.user_login_password_icon
                        ) as Drawable
                        when {
                            (text != null) -> text?.clear()
                        }
                        hideClearButton()
                        showUserIcon()
                        return true
                    }

                    else -> return false
                }
            } else return false
        }
        return false
    }


    override fun setAlpha(alpha: Float) {
        super.setAlpha(alpha)
        invalidate()
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun showUserIcon() {
        setButtonDrawables(startOfTheText = iconUserImage)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }
}