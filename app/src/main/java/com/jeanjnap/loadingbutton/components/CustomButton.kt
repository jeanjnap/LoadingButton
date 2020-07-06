package com.jeanjnap.loadingbutton.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.core.content.ContextCompat
import com.jeanjnap.loadingbutton.R
import com.jeanjnap.loadingbutton.utils.animations.ResizeAnimationHeight
import com.jeanjnap.loadingbutton.utils.extensions.setBackgroundColorWithCorner
import com.jeanjnap.loadingbutton.utils.extensions.setLoadingBackground

class CustomButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var buttonWith: Int = width
    private var buttonHeight: Int = height
    private var buttonText: CharSequence = EMPTY_TEXT
    private var runningAnimation = false

    var buttonColor: Int = ContextCompat.getColor(context, R.color.colorPrimary)
        set(value) {
            setBackgroundColorWithCorner(value, cornerRadius)
            field = value
        }
    var cornerRadius: Int = context.resources.getDimensionPixelSize(R.dimen.eight_value)
        set(value) {
            setBackgroundColorWithCorner(buttonColor, value)
            field = value
        }
    var progressBarColor: Int = ContextCompat.getColor(context, R.color.white)

    init {
        post {
            buttonText = text
            buttonWith = width
            buttonHeight = height
        }

        attrs?.let { it ->
            val attributes = context.obtainStyledAttributes(
                it,
                R.styleable.CustomButton,
                DEFAULT_STYLE_ATTRIBUTE,
                DEFAULT_STYLE_RESOURCE
            )

            buttonColor = attributes.getColor(R.styleable.CustomButton_buttonColor, buttonColor)
            cornerRadius = attributes.getInt(R.styleable.CustomButton_cornerRadius, cornerRadius)
            progressBarColor =
                attributes.getColor(R.styleable.CustomButton_progressBarColor, buttonColor)

            var drawableBackground: Drawable

            val tempDrawable = ContextCompat.getDrawable(getContext(), R.drawable.circular_progress)!!.let {
                    when (it) {
                        is ColorDrawable -> GradientDrawable().apply { setColor(it.color) }
                        else -> it
                    }
                }
            drawableBackground = tempDrawable.let {
                it.constantState?.newDrawable()?.mutate() ?: it
            }

            setBackground(drawableBackground)

            attributes.recycle()
        }
    }

    fun startAnimation() {
        startAnimation(ResizeAnimationHeight(
            this,
            buttonHeight,
            {
                Log.e("_TEST", "Start animation")
                setBackgroundColorWithCorner(buttonColor, buttonHeight.div(TWO_VALUE))
                isClickable = false
                text = EMPTY_TEXT
            },
            {
                Log.e("_TEST", "End animation")
                runningAnimation = true
            }
        ))
    }

    fun revertAnimation() {
        startAnimation(ResizeAnimationHeight(
            this,
            buttonWith,
            {
                Log.e("_TEST", "Start animation")
                runningAnimation = false
            },
            {
                Log.e("_TEST", "End animation")
                isClickable = true
                text = buttonText
                setBackgroundColorWithCorner(buttonColor, cornerRadius)
            }
        ))
    }


    companion object {
        private const val DEFAULT_STYLE_ATTRIBUTE = 0
        private const val DEFAULT_STYLE_RESOURCE = 0
        private const val EMPTY_TEXT = ""
        private const val TWO_VALUE = 2
    }
}