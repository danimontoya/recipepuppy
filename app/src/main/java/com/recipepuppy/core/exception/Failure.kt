package com.recipepuppy.core.exception

/**
 * Created by danieh on 29/07/2019.
 */
sealed class Failure {

    abstract class BaseFailure : Failure()

    class NetworkConnection : BaseFailure()
    class ServerError(val throwable: Throwable?) : BaseFailure()
    class PostsUsersError : BaseFailure()
    class BodyNullError : BaseFailure()
}