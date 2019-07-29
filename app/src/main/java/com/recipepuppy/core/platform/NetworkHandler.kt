package com.recipepuppy.core.platform

import android.content.Context
import com.recipepuppy.core.extension.networkInfo
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by danieh on 29/07/2019.
 */
@Singleton
class NetworkHandler
@Inject constructor(private val context: Context) {

    val isConnected get() = context.networkInfo?.isConnectedOrConnecting
}
