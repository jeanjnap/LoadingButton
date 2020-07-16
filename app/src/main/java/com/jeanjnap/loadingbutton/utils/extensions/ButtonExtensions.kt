package com.jeanjnap.loadingbutton.utils.extensions

import android.graphics.drawable.GradientDrawable
import androidx.appcompat.widget.AppCompatButton

fun AppCompatButton.setBackgroundColorWithCorner(color: Int, cornerRadius: Int) {
    background = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        this.cornerRadius = cornerRadius.toFloat()
        setColor(color)
    }
}
