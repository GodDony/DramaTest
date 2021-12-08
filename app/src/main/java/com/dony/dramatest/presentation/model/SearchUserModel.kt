package com.dony.dramatest.presentation.model

data class SearchUserModel(
    var total_count: Int,
    var incomplete_results: Boolean,
    var items: ArrayList<UserModel>
)