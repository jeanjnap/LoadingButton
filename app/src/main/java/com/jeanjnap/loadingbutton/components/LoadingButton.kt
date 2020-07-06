package com.jeanjnap.loadingbutton.components

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.jeanjnap.loadingbutton.R
import com.jeanjnap.loadingbutton.utils.animations.ResizeAnimationHeight
import com.jeanjnap.loadingbutton.utils.extensions.setBackgroundColorWithCorner
import kotlinx.android.synthetic.main.loading_button.view.bt_loading
import kotlinx.android.synthetic.main.loading_button.view.pb_loading

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = DEFAULT_STYLE_ATTRIBUTE
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var text: String = DEFAULT_TEXT
        set(value) {
            field = value
            bt_loading.text = value
        }
    var buttonColor: Int = ContextCompat.getColor(context, R.color.colorPrimary)
        set(value) {
            bt_loading.setBackgroundColorWithCorner(value, cornerRadius)
            field = value
        }
    var cornerRadius: Int = context.resources.getDimensionPixelSize(R.dimen.eight_value)
        set(value) {
            bt_loading.setBackgroundColorWithCorner(buttonColor, value)
            field = value
        }
    var textColor: Int = ContextCompat.getColor(context, R.color.white)
        set(value) {
            bt_loading.setTextColor(value)
            field = value
        }
    var progressBarColor: Int = ContextCompat.getColor(context, R.color.white)

    private var buttonWith: Int = width
    private var buttonHeight: Int = height

    init {
        LayoutInflater.from(context).inflate(R.layout.loading_button, this, true)

        bt_loading.post {
            buttonWith = bt_loading.width
            buttonHeight = bt_loading.height
        }

        /*attrs?.let {
            val attributes = context.obtainStyledAttributes(
                it,
                R.styleable.LoadingButton,
                DEFAULT_STYLE_ATTRIBUTE,
                DEFAULT_STYLE_RESOURCE
            )

            text = attributes.getString(R.styleable.LoadingButton_text) ?: DEFAULT_TEXT
            buttonColor = attributes.getColor(R.styleable.LoadingButton_buttonColor, buttonColor)
            textColor = attributes.getColor(R.styleable.LoadingButton_textColor, buttonColor)
            progressBarColor =
                attributes.getColor(R.styleable.LoadingButton_progressBarColor, buttonColor)

            attributes.recycle()
        }*/
    }

    fun startAnimation() {
        bt_loading.startAnimation(ResizeAnimationHeight(
            bt_loading,
            buttonHeight,
            {
                Log.e("_TEST", "Start animation")
                bt_loading.setBackgroundColorWithCorner(buttonColor, buttonHeight.div(2))
                bt_loading.text = ""
            },
            {
                Log.e("_TEST", "End animation")
                pb_loading.visibility = View.VISIBLE
            }
        ))
    }

    fun revertAnimation() {
        bt_loading.startAnimation(ResizeAnimationHeight(
            bt_loading,
            buttonWith,
            {
                Log.e("_TEST", "Start animation")
                pb_loading.visibility = View.GONE
            },
            {
                Log.e("_TEST", "End animation")
                bt_loading.text = text
                bt_loading.setBackgroundColorWithCorner(buttonColor, cornerRadius)
            }
        ))
    }

    companion object {
        private const val DEFAULT_STYLE_ATTRIBUTE = 0
        private const val DEFAULT_STYLE_RESOURCE = 0
        private const val DEFAULT_TEXT = "Button"
        private const val EMPTY_TEXT = ""
    }
}