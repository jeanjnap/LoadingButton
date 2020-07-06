package com.jeanjnap.loadingbutton

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jeanjnap.loadingbutton.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: ViewModel
    private var buttonHeight = 0
    private var buttonWith = 0
    private var expanded = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModel()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        lifecycle.addObserver(viewModel)

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.loading.observe(this, Observer {
            if (it) binding.button.startAnimation() else binding.button.revertAnimation()
        })
    }
}
