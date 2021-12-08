package com.dony.dramatest.presentation.searchuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.dony.dramatest.R
import com.dony.dramatest.base.BaseFragment
import com.dony.dramatest.databinding.FragmentSearchUserBinding
import com.dony.dramatest.extention.failure
import com.dony.dramatest.extention.observe
import com.dony.dramatest.extention.toGone
import com.dony.dramatest.extention.toVisible
import com.dony.dramatest.presentation.model.SearchUserModel
import com.dony.dramatest.presentation.searchuser.adapter.SearchUserAdapter
import com.dony.dramatest.presentation.searchuser.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 유저 검색 프래그먼트
 *
 * 검색 API 데이터를 가져온다. (검색 키워드 입력 후 검색 버튼 클릭 시)
 * Local DB 데이터를 저장한다. (목록 아이템 클릭 시)
 * Local DB 데이터를 삭제한다. (목록 아이템 클릭 시)
 */
@AndroidEntryPoint
class SearchUserFragment : BaseFragment() {
    @Inject
    lateinit var searchUserAdapter: SearchUserAdapter
    private lateinit var binding: FragmentSearchUserBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_user, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(recyclerView) {
                adapter = searchUserAdapter.apply {
                    // 즐겨찾기 설정
                    setBookmark {
                        // 어댑터에서 클릭한 유저 데이터 DB 저장하기
                        viewModel.cacheUser(it.isBookmark!!, it)
                    }
                }
            }

            with(btnApply) {
                setOnClickListener {
                    // 검색 클릭
                    val keyword = editSearch.text.toString()
                    if (keyword.isEmpty()) {
                        // 예외처리
                        R.string.error_empty_search.showToast()
                        return@setOnClickListener
                    }
                    progress.toVisible()
                    viewModel.searchUsers(keyword)
                }
            }
        }

        with(viewModel) {
            // 구독 설정
            observe(searchUserLiveData, ::bindSearchUser)
            observe(bookmarkLiveData, ::bindRemoveBookmark)
            failure(failureLiveData, ::handleError)
        }
    }

    override fun onResume() {
        super.onResume()
        with(viewModel) {
            // 액티비티에 타이틀 데이터 전달하기
            titleLiveData.value = getString(R.string.term_home)
        }
    }

    /**
     * 검색 결과
     */
    private fun bindSearchUser(model: SearchUserModel?) {
        with(binding) {
            beginDelayedTransition(layoutBase)
            model?.run {
                progress.toGone()
                if (total_count == 0) {
                    // 검색 결과 없음.
                    txtEmpty.toVisible()
                    recyclerView.toGone()
                } else {
                    // 검색 결과 있음.
                    txtEmpty.toGone()
                    recyclerView.toVisible()
                }

                items.run {
                    searchUserAdapter.putItems(this)
                }
            }
        }
    }

    /**
     * 즐겨찾기 프래그먼트에서 제거된 아이디를 전달받음
     *
     * 요구사항 : 사용자 검색 화면에서 해당 사용자 아이템 뷰의 즐겨찾기 여부를 갱신
     */
    private fun bindRemoveBookmark(id: Int?) {
        id?.run {
            searchUserAdapter.removeBookmark(this)
        }
    }

    /**
     * 네트워크 실패처리
     */
    private fun handleError(throwable: Throwable?) {
        binding.progress.toGone()
        onErrorHandle(throwable!!) { error ->
            when (error.code) {
                400 -> R.string.error_network_params.showToast()
                500 -> R.string.error_network_server.showToast()
                else -> R.string.error_exception.showToast()
            }
        }
    }

    companion object {
        fun newInstance() = SearchUserFragment()
    }
}