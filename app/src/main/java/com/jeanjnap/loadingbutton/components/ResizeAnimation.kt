package com.jeanjnap.loadingbutton.components

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class ResizeAnimation(
        private val view: View,
        private val expectedWidth: Int = view.width,
        private val expectedHeight: Int = view.height,
        val doOnStart: (() -> Unit)? = null,
        val doOnEnd: (() -> Unit)? = null
) : Animation() {

    private val initialWidth: Int = view.width
    private val initialHeight: Int = view.height

    init {
        duration = DURATION
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

    override fun applyTransformation(interpolatedTime: Float, transformation: Transformation) {
        view.layoutParams.width = (initialWidth + (expectedWidth - initialWidth) * interpolatedTime).toInt()
        view.layoutParams.height = (initialHeight + (expectedHeight - initialHeight) * interpolatedTime).toInt()
        view.requestLayout()
    }

    companion object {
        private const val DURATION = 400L
    }
}
