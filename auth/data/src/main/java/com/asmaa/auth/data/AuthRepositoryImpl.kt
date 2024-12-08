package com.asmaa.auth.data

import com.asmaa.auth.domain.AuthRepository
import com.asmaa.core.data.networking.post
import com.asmaa.core.domain.AuthInfo
import com.asmaa.core.domain.SessionStorage
import com.asmaa.core.domain.util.DataError
import com.asmaa.core.domain.util.EmptyResult
import com.asmaa.core.domain.util.Result
import com.asmaa.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): EmptyResult<DataError.NetworkError> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/login",
            LoginRequest(email = email, password = password)
        )
        if (result is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    userID = result.data.userId
                )
            )
        }
        return result.asEmptyDataResult()
    }

    override suspend fun register(
        email: String,
        password: String
    ): EmptyResult<DataError.NetworkError> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/register",
            body = RegisterRequest(email = email, password = password)
        )
    }
}