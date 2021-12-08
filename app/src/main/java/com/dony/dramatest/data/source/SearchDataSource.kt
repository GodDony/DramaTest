package com.dony.dramatest.data.source

import com.dony.dramatest.data.local.entity.UserEntity
import com.dony.dramatest.data.remote.response.SearchUsersResponse
import com.dony.dramatest.domain.model.User
import io.reactivex.Single

interface SearchDataSource {
    // 유저 검색
    fun searchUsers(loginId: String, page: Int, perPage: Int): Single<SearchUsersResponse>

    // 유저 저장
    fun cacheInsertUser(user: UserEntity)

    // 유저 삭제
    fun cacheDeleteUser(user: UserEntity)

    // 모든 유저 가져오기
    fun cacheGetLoadAllUsers(): MutableList<UserEntity>

    // 모든 유저 삭제
    fun cacheDeleteAllUsers()

    // 유저 가져오기
    fun cacheGetUser(id: Int): UserEntity

    // 유저 검색
    fun cacheSearchUsers(login: String): MutableList<UserEntity>
}