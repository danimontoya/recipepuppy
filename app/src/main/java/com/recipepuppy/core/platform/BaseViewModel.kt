package com.recipepuppy.core.platform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recipepuppy.core.exception.Failure

/**
 * Created by danieh on 29/07/2019.
 *
 * Base ViewModel class with default Failure handling.
 */
abstract class BaseViewModel : ViewModel() {

    var failure: MutableLiveData<Failure> = MutableLiveData()

    protected fun handleFailure(failure: Failure?) {
        this.failure.value = failure
    }
}