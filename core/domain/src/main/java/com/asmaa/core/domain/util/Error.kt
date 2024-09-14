package com.asmaa.core.domain.util

interface Error {
}

sealed interface DataError : Error {
    enum class NetworkError : DataError {
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        FORBIDDEN,
        CONFLICT,
        NOT_FOUND,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN_ERROR
    }

    enum class Local : DataError {
        DISK_FULL
    }
}