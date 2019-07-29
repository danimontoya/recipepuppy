package com.recipepuppy.core.extension

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by danieh on 29/07/2019.
 */
val Context.networkInfo: NetworkInfo?
    get() = (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
