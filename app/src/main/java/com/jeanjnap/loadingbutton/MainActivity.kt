package com.jeanjnap.loadingbutton

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jeanjnap.loadingbutton.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModel()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        lifecycle.addObserver(viewModel)

        binding.button.buttonColor = ContextCompat.getColor(this, R.color.colorAccent)
        binding.button.spinningBarWidth = DEFAULT_SPINNER_WIDTH
        binding.button.paddingProgress = DEFAULT_SPINNER_PADDING
        binding.button.cornerRadius = resources.getDimensionPixelSize(R.dimen.eight_value)
        binding.button.progressBarColor = ContextCompat.getColor(this, R.color.white)

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.loading.observe(this, Observer {
            if (it) binding.button.startAnimation() else binding.button.revertAnimation()
        })
    }

    companion object {
        private const val DEFAULT_SPINNER_WIDTH = 3F
        private const val DEFAULT_SPINNER_PADDING = 5F
    }
}
