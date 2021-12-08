package com.dony.dramatest.data.repository

import com.dony.dramatest.domain.model.SearchUser
import com.dony.dramatest.domain.model.User
import io.reactivex.Single

interface SearchRepository {
    // 유저 검색
    fun searchUsers(loginId: String, page: Int, perPage: Int): Single<SearchUser>

    // 유저 저장
    fun cacheInsertUser(user: User)

    // 유저 삭제
    fun cacheDeleteUser(user: User)

    // 모든 유저 가져오기
    fun cacheGetLoadAllUsers(): ArrayList<User>

    // 모든 유저 삭제
    fun cacheDeleteAllUsers()

    // 유저 가져오기
    fun cacheGetUser(id: Int): User

    // 유저 검색
    fun cacheSearchUsers(login: String): ArrayList<User>
}