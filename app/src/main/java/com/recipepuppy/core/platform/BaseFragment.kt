package com.recipepuppy.core.platform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.recipepuppy.RecipePuppyApp
import com.recipepuppy.core.di.ApplicationComponent
import com.recipepuppy.core.extension.viewContainer
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

/**
 * Created by danieh on 29/07/2019.
 */
abstract class BaseFragment : Fragment() {

    abstract fun layoutId(): Int

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity?.application as RecipePuppyApp).appComponent
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(layoutId(), container, false)

    internal fun notify(message: String) = Snackbar.make(viewContainer, message, Snackbar.LENGTH_LONG).show()
}
