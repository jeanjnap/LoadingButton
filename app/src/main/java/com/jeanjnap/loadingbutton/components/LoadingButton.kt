package com.jeanjnap.loadingbutton.components

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.jeanjnap.loadingbutton.R
import com.jeanjnap.loadingbutton.utils.extensions.setBackgroundColorWithCorner

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = DEFAULT_STYLE_ATTRIBUTE
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var initialWidth = width
    private var buttonText = text
    private var showProgress = false
    private var runningAnimation = false

    var spinningBarWidth = DEFAULT_SPINNER_WIDTH
    var paddingProgress = DEFAULT_SPINNER_PADDING

    var buttonColor: Int = ContextCompat.getColor(context, R.color.colorAccent)
        set(value) {
            if (!runningAnimation) setBackgroundColorWithCorner(value, cornerRadius)
            field = value
        }
    var cornerRadius: Int = context.resources.getDimensionPixelSize(R.dimen.eight_value)
        set(value) {
            if (!runningAnimation) setBackgroundColorWithCorner(buttonColor, value)
            field = value
        }
    var progressBarColor: Int = ContextCompat.getColor(context, R.color.white)

    init {
        post {
            buttonText = text
            initialWidth = width
        }

        attrs?.let { it ->
            val attributes = context.obtainStyledAttributes(
                it,
                R.styleable.LoadingButton,
                DEFAULT_STYLE_ATTRIBUTE,
                DEFAULT_STYLE_RESOURCE
            )

            buttonColor = attributes.getColor(R.styleable.LoadingButton_buttonColor, buttonColor)
            cornerRadius = attributes.getInt(R.styleable.LoadingButton_cornerRadius, cornerRadius)
            progressBarColor =
                attributes.getColor(R.styleable.LoadingButton_progressBarColor, progressBarColor)
            spinningBarWidth =
                attributes.getDimension(
                    R.styleable.LoadingButton_spinningBarWidth,
                    spinningBarWidth
                )
            paddingProgress =
                attributes.getDimension(R.styleable.LoadingButton_paddingProgress, paddingProgress)

            attributes.recycle()
        }
    }

    fun startAnimation() {
        startAnimation(
            ResizeAnimation(
                button = this,
                expectedWidth = height,
                expectedHeight = height,
                doOnStart = {
                    runningAnimation = true
                    setBackgroundColorWithCorner(buttonColor, height.div(TWO_VALUE))
                    isClickable = false
                    buttonText = text
                    text = EMPTY_TEXT
                },
                doOnEnd = {
                    runningAnimation = false
                    showProgress = true
                }
            )
        )
    }

    fun revertAnimation() {
        startAnimation(ResizeAnimation(
            button = this,
            expectedHeight = height,
            expectedWidth = initialWidth,
            doOnStart = {
                runningAnimation = true
                showProgress = false
            },
            doOnEnd = {
                runningAnimation = false
                isClickable = true
                text = buttonText
                setBackgroundColorWithCorner(buttonColor, cornerRadius)
            }
        ))
    }

    private val animatedProgressBarDrawable: AnimatedProgressBarDrawable by lazy {
        AnimatedProgressBarDrawable(this, spinningBarWidth, progressBarColor).applyPadding(
            paddingProgress.toInt()
        )
    }

    private fun drawProgress(canvas: Canvas) {
        animatedProgressBarDrawable.drawProgress(canvas)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (showProgress)
            drawProgress(canvas)
    }

    companion object {
        private const val DEFAULT_STYLE_ATTRIBUTE = 0
        private const val DEFAULT_STYLE_RESOURCE = 0
        private const val TWO_VALUE = 2
        private const val DEFAULT_SPINNER_WIDTH = 3F
        private const val DEFAULT_SPINNER_PADDING = 5F
        const val EMPTY_TEXT = ""
    }
}