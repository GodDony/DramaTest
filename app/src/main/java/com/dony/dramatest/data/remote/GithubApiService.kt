package com.dony.dramatest.data.remote

import com.dony.dramatest.data.remote.response.SearchUsersResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {
    // 검색 - 유저 리스트
    @GET(GithubApiConstants.SEARCH_USERS)
    fun requestSearchUsers(@Query("q") q: String, @Query("page") page: Int, @Query("per_page") per_page: Int): Single<SearchUsersResponse>
}

object GithubApiConstants {
    const val BASE_URL = "https://api.github.com"
    const val SEARCH_USERS = "search/users"
}