package com.dony.dramatest.data.remote.response

data class SearchUsersResponse(
    var total_count: Int,
    var incomplete_results: Boolean,
    var items: ArrayList<UserResponse>
)