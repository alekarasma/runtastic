package com.asmaa.core.presentation.ui

import com.asmaa.core.domain.util.DataError

fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Local.DISK_FULL -> UiText.StringResource(R.string.disk_full)
        DataError.NetworkError.REQUEST_TIMEOUT -> UiText.StringResource(R.string.request_timeout)
        DataError.NetworkError.TOO_MANY_REQUESTS -> UiText.StringResource(R.string.too_many_requests)
        DataError.NetworkError.NO_INTERNET -> UiText.StringResource(R.string.no_internet)
        DataError.NetworkError.PAYLOAD_TOO_LARGE -> UiText.StringResource(R.string.payload_too_large)
        DataError.NetworkError.SERVER_ERROR -> UiText.StringResource(R.string.server_error)
        DataError.NetworkError.SERIALIZATION -> UiText.StringResource(R.string.serialization)
        else -> UiText.StringResource(R.string.unknown_error)
    }

}