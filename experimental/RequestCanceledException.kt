package com.example.skills.experimental

class RequestCanceledException(
    val permission: Permission,
    message: String? = null
) : Exception(message)