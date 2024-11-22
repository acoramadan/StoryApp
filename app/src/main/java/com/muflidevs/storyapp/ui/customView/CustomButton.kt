package com.muflidevs.storyapp.ui.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.muflidevs.storyapp.R

class CustomButton : AppCompatButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var txtColor: Int = 0
    private var enabledBackground: Drawable
    private var disabledBackground: Drawable

    init {
        txtColor = ContextCompat.getColor(context, R.color.white)
        enabledBackground =
            ContextCompat.getDrawable(context, R.drawable.custom_bg_button) as Drawable
        disabledBackground =
            ContextCompat.getDrawable(context, R.drawable.custom_bg_btn_disable) as Drawable

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledBackground else disabledBackground
        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
        text =
            if (isEnabled) context.getText(R.string.submit) else context.getText(R.string.btn_validation)
    }
}