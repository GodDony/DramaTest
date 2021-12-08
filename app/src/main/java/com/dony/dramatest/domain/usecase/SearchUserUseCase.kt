package com.dony.dramatest.domain.usecase

import com.dony.dramatest.base.SingleUseCase
import com.dony.dramatest.data.mapper.toDomain
import com.dony.dramatest.data.mapper.toPresentation
import com.dony.dramatest.data.remote.request.SearchUserListParamRequest
import com.dony.dramatest.data.repository.SearchRepository
import com.dony.dramatest.presentation.model.SearchUserModel
import com.dony.dramatest.presentation.model.UserModel
import io.reactivex.Single
import javax.inject.Inject

/**
 *  UseCase 클래스에서는 Presentation 단위로 맵핑 한다.
 *
 *  Remote Output Mapper Domain -> Presentation
 *  Local Output Mapper Domain -> Presentation
 *  Local Input Mapper Presentation -> Domain
 */
class SearchUserUseCase @Inject constructor(
    private val repository: SearchRepository
) : SingleUseCase<SearchUserModel, SearchUserListParamRequest>() {

    override fun buildUseCaseSingle(params: SearchUserListParamRequest?): Single<SearchUserModel> {
        return repository.searchUsers(
            loginId = params!!.loginId,
            page = params.page,
            perPage = params.perPage
        ).map {
            // API 로 넘어온 데이터 맵핑
            it.toPresentation().apply {
                // 요구사항 : 사용자가 이미 즐겨찾기에 추가되어 있으면 이를 표시합니다.
                cacheGetLoadAllUsers().forEach { localUser ->
                    // 저장된 유저 목록 루프
                    this.items.forEach { remoteUser ->
                        // API 로 넘어온 유저 목록 루프
                        if (localUser.id == remoteUser.id) {
                            // 즐겨찾기에 추가 되어 있는 유저 표시하기
                            remoteUser.isBookmark = true
                        }
                    }
                }
                this.items = makeHeader(this.items)
            }
        }
    }

    /**
     * 리스트 헤더 만들기
     *
     * 요구사항 : 사용자 이름 순으로 정렬합니다. 사용자 이름의 초성이나
     * 알파벳을 기준으로 헤더를 붙여줍니다. (리멤버에서 이름순
     * 정렬 시 ᄀ,ᄂ,ᄃ 형식의 헤더 참고)
     */
    private fun makeHeader(items: ArrayList<UserModel>): ArrayList<UserModel> {
        // 정렬
        val copyItems = items.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.login })).toMutableList() as ArrayList<UserModel>
        val customItems = ArrayList<UserModel>()
        var lastHeader = ""

        copyItems.forEach {
            val header = it.login[0].toUpperCase().toString()
            if (header != lastHeader) {
                // 헤더 추가
                lastHeader = header
                // UserModel Copy
                customItems.add(it.copy().apply {
                    // Domain Layer 에서 정의된 필드가 아님
                    // 화면 단위에서 사용할 Presentation Layer 에 정의된 필드이기 때문에 값을 변경해준다.
                    // 헤더 값 설정
                    this.header = lastHeader
                    // 뷰타입 설정
                    viewType = 1
                })
            }
            // 유저 추가
            customItems.add(it)
        }
        return customItems
    }

    // 유저 저장
    fun cacheInsertUser(user: UserModel) {
        repository.cacheInsertUser(user.toDomain())
    }

    // 유저 삭제
    fun cacheDeleteUser(user: UserModel) {
        repository.cacheDeleteUser(user.toDomain())
    }

    // 모든 유저 가져오기
    fun cacheGetLoadAllUsers(): ArrayList<UserModel> {
        return makeHeader(repository.cacheGetLoadAllUsers().map {
            it.toPresentation().apply {
                // isBookmark 값 변경하기
                // Domain Layer 에서 정의된 필드가 아님
                // 화면 단위에서 사용할 Presentation Layer 에 정의된 필드이기 때문에 값을 변경해준다.
                isBookmark = true
            }
        }.toMutableList() as ArrayList<UserModel>)
    }

    // 모든 유저 삭제
    fun cacheDeleteAllUsers() {
        repository.cacheDeleteAllUsers()
    }

    // 유저 가져오기
    fun cacheGetUser(id: Int): UserModel {
        return repository.cacheGetUser(id).toPresentation()
    }

    // 유저 검색
    fun cacheSearchUsers(login: String): ArrayList<UserModel> {
        return makeHeader(repository.cacheSearchUsers("%$login%").map {
            it.toPresentation().apply {
                // isBookmark 값 변경하기
                // Domain Layer 에서 정의된 필드가 아님
                // 화면 단위에서 사용할 Presentation Layer 에 정의된 필드이기 때문에 값을 변경해준다.
                isBookmark = true
            }
        }.toMutableList() as ArrayList<UserModel>)
    }
}