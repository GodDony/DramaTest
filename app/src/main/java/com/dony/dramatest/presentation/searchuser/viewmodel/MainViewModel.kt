package com.dony.dramatest.presentation.searchuser.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dony.dramatest.base.BaseViewModel
import com.dony.dramatest.data.remote.request.SearchUserListParamRequest
import com.dony.dramatest.domain.usecase.SearchUserUseCase
import com.dony.dramatest.presentation.model.SearchUserModel
import com.dony.dramatest.presentation.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: SearchUserUseCase
) : BaseViewModel() {
    val searchUserLiveData = MutableLiveData<SearchUserModel>()
    val titleLiveData = MutableLiveData<String>()
    val bookmarkLiveData = MutableLiveData<Int>()

    /**
     * 유저 아이디로 검색하기
     *
     * 요구사항 : 검색 결과는 최대 100까지만 보여줍니다. 이를 위해 검색 API
     * 호출 시 page는 1, per_page는 100으로 설정합니다. 자세한
     * 내용은 Github 검색 API 문서를 참고해주세요.
     */
    fun searchUsers(loginId: String) {
        useCase.execute(
            SearchUserListParamRequest(
                loginId = loginId,
                page = 1,
                perPage = 100
            ),
            onSuccess = ::handleSearchUsers,
            onError = ::handleFailure
        )
    }

    /**
     * 유저 정보 저장 및 삭제
     *
     * 요구사항 : 아이템 뷰를 누르면 즐겨찾기로 추가합니다. 이미 즐겨찾기된
     * 사용자의 아이템 뷰를 누르면 즐겨찾기를 취소합니다. 사용자
     * 검색 화면에서 해당 사용자 아이템 뷰의 즐겨찾기 여부를
     * 갱신하고, 즐겨찾기 화면에서도 해당 사용자를 추가 또는
     * 삭제합니다.
     */
    fun cacheUser(isBookmark: Boolean, userModel: UserModel) {
        if (isBookmark) {
            // 추가
            useCase.cacheInsertUser(userModel)
        } else {
            // 삭제
            useCase.cacheDeleteUser(userModel)
        }
    }

    /**
     * 저장된 유저 목록 가져오기
     */
    fun cacheLoadAllUser(): ArrayList<UserModel> {
        return useCase.cacheGetLoadAllUsers()
    }

    /**
     * 저장된 유저 검색
     *
     * 요구사항 : 검색어 입력창에 검색어를 입력하고 검색 버튼을 클릭하면,
     * 로컬 DB에서 이름에 검색어가 포함된 사용자를 검색합니다.
     * 검색 필드는 사용자 이름으로 제한합니다.
     */
    fun cacheSearchUsers(login: String): ArrayList<UserModel> {
        return useCase.cacheSearchUsers(login)
    }

    /**
     * 검색 결과 처리
     */
    private fun handleSearchUsers(model: SearchUserModel?) {
        model?.run {
            searchUserLiveData.value = this
        }
    }
}