package com.jeanjnap.loadingbutton.utils.extensions

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import com.jeanjnap.loadingbutton.R

fun View.setBackgroundColorWithCorner(color: Int, cornerRadius: Int) {
    background = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        this.cornerRadius = cornerRadius.toFloat()
        setColor(color)
    }
}

fun View.setLoadingBackground(color: Int) {
    background = GradientDrawable().apply {
        shape = GradientDrawable.RING
        useLevel = false
        this.setGradientCenter(0.5f, 0.5f)
        this.colors = intArrayOf(color, ContextCompat.getColor(context, R.color.transparent))
    }
}