package com.bkplus.callscreen.api

sealed class BaseResponse<out R> {
    data class Success<out R>(val result: R) : BaseResponse<R>()
    data class Failure<out R>(val code: Int, val message: String?) : BaseResponse<R>()
    data class UnknownError(val throwable: Throwable?) : BaseResponse<Nothing>()
}

fun <T : Any> BaseResponse<T>.onSuccess(
    onSuccess: (T) -> Unit
): BaseResponse<T> = apply {
    if (this is BaseResponse.Success) onSuccess(this.result)
}

fun <T : Any> BaseResponse<T>.onFailure(
    onFail: (code: Int, message: String?) -> Unit
): BaseResponse<T> = apply {
    if (this is BaseResponse.Failure) onFail(code, message)
}

fun <T : Any> BaseResponse<T>.onException(
    onException: (throwable: Throwable?) -> Unit
): BaseResponse<T> = apply {
    if (this is BaseResponse.UnknownError) onException(throwable)
}

fun <T : Any> BaseResponse<T>.handleResponse(
    onSuccess: (T) -> Unit,
    onFailure: ((Int, String?) -> Unit?)? = null,
    onUnknownError: ((Throwable?) -> Unit)? = null
) {
    when (this) {
        is BaseResponse.Success -> {
            onSuccess(this.result)
        }

        is BaseResponse.Failure -> {
            if (onFailure != null) {
                onFailure(this.code, this.message)
            }
        }

        is BaseResponse.UnknownError -> {
            if (onUnknownError != null) {
                onUnknownError(this.throwable)
            }
        }
    }
}
