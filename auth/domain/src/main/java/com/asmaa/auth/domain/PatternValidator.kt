package com.asmaa.auth.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}