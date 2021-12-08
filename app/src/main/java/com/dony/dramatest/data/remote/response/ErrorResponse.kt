package com.dony.dramatest.data.remote.response

data class ErrorResponse(
    var message: String,
    var code: Int,
    var type: String
)