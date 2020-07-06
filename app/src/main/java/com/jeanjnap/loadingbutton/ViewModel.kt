package com.jeanjnap.loadingbutton

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class ViewModel : ViewModel(), LifecycleObserver {

    val loading: LiveData<Boolean> get() = _loading

    private val _loading = MutableLiveData<Boolean>()

    fun onButtonClick() {
        launchDataLoad {
            delay(1500)
        }
    }

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun launchDataLoad(
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return uiScope.launch {
            try {
                _loading.value = true
                block()
            } catch (error: Exception) {
                error.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }
}
