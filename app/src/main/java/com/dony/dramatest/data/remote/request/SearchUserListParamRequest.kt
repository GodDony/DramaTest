package com.dony.dramatest.data.remote.request

data class SearchUserListParamRequest(
    val loginId: String,
    val page: Int,
    val perPage: Int
)