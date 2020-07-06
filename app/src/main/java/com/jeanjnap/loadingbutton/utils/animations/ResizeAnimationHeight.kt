package com.jeanjnap.loadingbutton.utils.animations

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class ResizeAnimationHeight(
    private val view: View,
    private val expectedWidth: Int,
    val doOnStart: (() -> Unit)? = null,
    val doOnEnd: (() -> Unit)? = null
) : Animation() {

    init {
        duration =
            DURATION
        setAnimationListener(object : AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                // Nothing to do here
            }

            override fun onAnimationEnd(animation: Animation?) {
                doOnEnd?.invoke()
            }

            override fun onAnimationStart(animation: Animation?) {
                doOnStart?.invoke()
            }
        })
    }

    private val initialWidth: Int = view.width

    override fun applyTransformation(interpolatedTime: Float, transformation: Transformation) {
        view.layoutParams.width = (initialWidth + (expectedWidth - initialWidth) * interpolatedTime).toInt()
        view.requestLayout()
    }

    companion object {
        private const val DURATION = 400L
    }
}