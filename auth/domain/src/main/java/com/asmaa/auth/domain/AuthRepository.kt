package com.asmaa.auth.domain

import com.asmaa.core.domain.util.DataError
import com.asmaa.core.domain.util.EmptyResult
import javax.xml.crypto.Data

interface AuthRepository {
    suspend fun login(email: String, password: String): EmptyResult<DataError.NetworkError>
    suspend fun register(email: String, password: String): EmptyResult<DataError.NetworkError>
}