package com.jeanjnap.loadingbutton.components

import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.appcompat.widget.AppCompatButton

class ResizeAnimation(
    private val button: AppCompatButton,
    private val expectedWidth: Int = button.width,
    private val expectedHeight: Int = button.height,
    val doOnStart: (() -> Unit)? = null,
    val doOnEnd: (() -> Unit)? = null
) : Animation() {

    private val initialWidth: Int = button.width
    private val initialHeight: Int = button.height

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
        button.layoutParams.width = (initialWidth + (expectedWidth - initialWidth) * interpolatedTime).toInt()
        button.layoutParams.height = (initialHeight + (expectedHeight - initialHeight) * interpolatedTime).toInt()
        button.requestLayout()
    }

    companion object {
        private const val DURATION = 400L
    }
}
