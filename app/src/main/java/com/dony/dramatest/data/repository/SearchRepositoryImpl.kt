package com.dony.dramatest.data.repository

import com.dony.dramatest.data.mapper.toDomain
import com.dony.dramatest.data.mapper.toEntity
import com.dony.dramatest.data.source.SearchDataSource
import com.dony.dramatest.domain.model.SearchUser
import com.dony.dramatest.domain.model.User
import io.reactivex.Single
import javax.inject.Inject

/**
 *  Repository 클래스에서는 Domain 단위로 맵핑 한다.
 *
 *  Remote Output Mapper Response -> Domain
 *  Local Output Mapper Entity -> Domain
 *  Local Input Mapper Domain -> Entity
 */
class SearchRepositoryImpl @Inject constructor(
    private val dataSource: SearchDataSource
) : SearchRepository {

    // 유저 검색
    override fun searchUsers(loginId: String, page: Int, perPage: Int): Single<SearchUser> {
        return dataSource.searchUsers(loginId, page, perPage).map { it.toDomain() }
    }

    // 유저 저장
    override fun cacheInsertUser(user: User) {
        dataSource.cacheInsertUser(user.toEntity())
    }

    // 유저 삭제
    override fun cacheDeleteUser(user: User) {
        dataSource.cacheDeleteUser(user.toEntity())
    }

    // 모든 유저 가져오기
    override fun cacheGetLoadAllUsers(): ArrayList<User> {
        return dataSource.cacheGetLoadAllUsers().map { it.toDomain() } as ArrayList<User>
    }

    // 모든 유저 삭제
    override fun cacheDeleteAllUsers() {
        dataSource.cacheDeleteAllUsers()
    }

    // 유저 가져오기
    override fun cacheGetUser(id: Int): User {
        return dataSource.cacheGetUser(id).toDomain()
    }

    // 유저 검색
    override fun cacheSearchUsers(login: String): ArrayList<User> {
        return dataSource.cacheSearchUsers(login).map { it.toDomain() } as ArrayList<User>
    }
}