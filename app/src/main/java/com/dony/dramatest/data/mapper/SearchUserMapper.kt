package com.dony.dramatest.data.mapper

import com.dony.dramatest.data.remote.response.SearchUsersResponse
import com.dony.dramatest.domain.model.SearchUser
import com.dony.dramatest.domain.model.User
import com.dony.dramatest.presentation.model.SearchUserModel
import com.dony.dramatest.presentation.model.UserModel

fun SearchUsersResponse.toDomain() = SearchUser(
    total_count = total_count,
    incomplete_results = incomplete_results,
    items = items.map { it.toDomain() } as ArrayList<User>
)

fun SearchUser.toPresentation() = SearchUserModel(
    total_count = total_count,
    incomplete_results = incomplete_results,
    items = items.map { it.toPresentation() } as ArrayList<UserModel>
)