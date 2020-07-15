package com.jeanjnap.loadingbutton

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        binding.button.buttonColor = Color.GRAY
        binding.button.spinningBarWidth = 3F
        binding.button.paddingProgress = 5F
        binding.button.cornerRadius = resources.getDimensionPixelSize(R.dimen.eight_value)
        binding.button.progressBarColor = Color.RED

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.loading.observe(this, Observer {
            if (it) binding.button.startAnimation() else binding.button.revertAnimation()
        })
    }
}
