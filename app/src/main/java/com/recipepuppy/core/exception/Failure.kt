package com.recipepuppy.core.exception

import java.lang.Exception

/**
 * Created by danieh on 29/07/2019.
 */
sealed class Failure {

    abstract class BaseFailure : Failure()

    class NetworkConnection : BaseFailure()
    class ServerError(val throwable: Throwable?) : BaseFailure()
    class BodyNullError : BaseFailure()
    class GetRecipesError : BaseFailure()
    class DbGetFavoriteRecipesError(val exception: Exception) : BaseFailure()
    class DbInsertError(val exception: Exception) : BaseFailure()
    class DbDeleteError(val exception: Exception) : BaseFailure()
}