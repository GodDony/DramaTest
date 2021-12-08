package com.dony.dramatest.data.source

import com.dony.dramatest.data.local.dao.UserDao
import com.dony.dramatest.data.local.entity.UserEntity
import com.dony.dramatest.data.remote.GithubApiService
import com.dony.dramatest.data.remote.response.SearchUsersResponse
import io.reactivex.Single
import javax.inject.Inject

/**
 * DataSource 클래스에서는 따로 데이터를 맵핑 하지 않는다.
 *
 * Remote Output Data -> Response
 * Local Output Data -> Entity
 * Local Input Data -> Entity
 */
class SearchDataSourceImpl @Inject constructor(
    private val apiService: GithubApiService,
    private val userDao: UserDao
) : SearchDataSource {

    // 유저 검색
    override fun searchUsers(loginId: String, page: Int, perPage: Int): Single<SearchUsersResponse> {
        return apiService.requestSearchUsers(loginId, page, perPage)
    }

    // 유저 저장
    override fun cacheInsertUser(user: UserEntity) {
        userDao.insert(user)
    }

    // 유저 삭제
    override fun cacheDeleteUser(user: UserEntity) {
        userDao.deleteUser(user)
    }

    // 모든 유저 가져오기
    override fun cacheGetLoadAllUsers(): MutableList<UserEntity> {
        return userDao.loadAll()
    }

    // 모든 유저 삭제
    override fun cacheDeleteAllUsers() {
        userDao.deleteAll()
    }

    // 유저 가져오기
    override fun cacheGetUser(id: Int): UserEntity {
        return userDao.getUser(id)
    }

    override fun cacheSearchUsers(login: String): MutableList<UserEntity> {
        return userDao.searchUsers(login)
    }
}