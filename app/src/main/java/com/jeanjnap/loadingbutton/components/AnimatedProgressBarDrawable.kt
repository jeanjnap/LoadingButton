package com.jeanjnap.loadingbutton.components

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator

internal class AnimatedProgressBarDrawable(
    private val button: LoadingButton,
    private val progressWidth: Float,
    progressColor: Int
) : Drawable(), Animatable {

    private val bound: RectF by lazy {
        RectF().apply {
            left = bounds.left.toFloat() + progressWidth * OFFSET_RATIO
            right = bounds.right.toFloat() - progressWidth * OFFSET_RATIO
            top = bounds.top.toFloat() + progressWidth * OFFSET_RATIO
            bottom = bounds.bottom.toFloat() - progressWidth * OFFSET_RATIO
        }
    }

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = progressWidth
        color = progressColor
    }

    private var currentGlobalAngle: Float = ZERO_VALUE_FLOAT
    private var currentSweepAngle: Float = ZERO_VALUE_FLOAT
    private var currentGlobalAngleOffset: Float = ZERO_VALUE_FLOAT
    private var modeAppearing: Boolean = false
    private var shouldDraw: Boolean = true

    private val indeterminateAnimator = AnimatorSet().apply {
        playTogether(
                angleValueAnimator(LinearInterpolator()),
                sweepValueAnimator(AccelerateDecelerateInterpolator())
        )
    }

    fun applyPadding(paddingProgress: Int) = this.apply {
        val offset = (button.width - button.height) / 2

        val padding = Rect()

        val left = offset + paddingProgress + padding.bottom
        val right = button.width - offset - paddingProgress - padding.bottom
        val bottom = button.height - paddingProgress - padding.bottom
        val top = paddingProgress + padding.top

        setBounds(left, top, right, bottom)
    }

    fun drawProgress(canvas: Canvas) {
        if (isRunning) {
            draw(canvas)
        } else {
            start()
        }
    }

    private fun toggleSweep() {
        modeAppearing = !modeAppearing

        if (modeAppearing) {
            currentGlobalAngleOffset =
                    (currentGlobalAngleOffset + MIN_SWEEP_ANGLE * TWO_VALUE) % ARCH_ANGLE
        }
    }

    private fun angleValueAnimator(timeInterpolator: TimeInterpolator): ValueAnimator =
            ValueAnimator.ofFloat(ZERO_VALUE_FLOAT, ARCH_ANGLE.toFloat()).apply {
                interpolator = timeInterpolator
                duration = ANGLE_ANIMATOR_DURATION
                repeatCount = ValueAnimator.INFINITE

                addUpdateListener { animation -> currentGlobalAngle = animation.animatedValue as Float }
            }

    private fun sweepValueAnimator(timeInterpolator: TimeInterpolator): ValueAnimator =
            ValueAnimator.ofFloat(
                    ZERO_VALUE_FLOAT,
                    ARCH_ANGLE.toFloat() - TWO_VALUE * MIN_SWEEP_ANGLE
            ).apply {
                interpolator = timeInterpolator
                duration = SWEEP_ANIMATOR_DURATION
                repeatCount = ValueAnimator.INFINITE

                addUpdateListener { animation ->
                    currentSweepAngle = animation.animatedValue as Float

                    if (currentSweepAngle < MIN_SWEEP_ANGLE_TO_DRAW) {
                        shouldDraw = true
                    }

                    if (shouldDraw) {
                        button.invalidate()
                    }
                }

                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationRepeat(animation: Animator) {
                        toggleSweep()
                        shouldDraw = false
                    }
                })
            }

    private fun getAngles(): Pair<Float, Float> {
        return if (modeAppearing) {
            (currentGlobalAngle - currentGlobalAngleOffset) to currentSweepAngle + MIN_SWEEP_ANGLE
        } else {
            (currentGlobalAngle - currentGlobalAngleOffset + currentSweepAngle) to
                    ARCH_ANGLE - currentSweepAngle - MIN_SWEEP_ANGLE
        }
    }


    override fun isRunning(): Boolean = indeterminateAnimator.isRunning

    override fun start() {
        if (isRunning) {
            return
        }

        indeterminateAnimator.start()
    }

    override fun stop() {
        if (!isRunning) {
            return
        }

        indeterminateAnimator.end()
    }

    override fun draw(canvas: Canvas) {
        val (startAngle, sweepAngle) = getAngles()
        canvas.drawArc(bound, startAngle, sweepAngle, false, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity() = PixelFormat.TRANSPARENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    companion object {
        private const val ANGLE_ANIMATOR_DURATION = 1500L
        private const val SWEEP_ANIMATOR_DURATION = 700L
        private const val MIN_SWEEP_ANGLE = 30F
        private const val OFFSET_RATIO = .5F
        private const val ZERO_VALUE_FLOAT = 0F
        private const val TWO_VALUE = 2
        private const val ARCH_ANGLE = 360
        private const val MIN_SWEEP_ANGLE_TO_DRAW = 5
    }
}
