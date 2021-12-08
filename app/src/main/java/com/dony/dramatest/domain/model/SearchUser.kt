package com.dony.dramatest.domain.model

data class SearchUser(
    var total_count: Int,
    var incomplete_results: Boolean,
    var items: ArrayList<User>
)