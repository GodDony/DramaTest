package com.dony.dramatest.presentation.searchuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.dony.dramatest.R
import com.dony.dramatest.base.BaseFragment
import com.dony.dramatest.databinding.FragmentBookmarkBinding
import com.dony.dramatest.extention.toGone
import com.dony.dramatest.extention.toVisible
import com.dony.dramatest.presentation.searchuser.adapter.SearchUserAdapter
import com.dony.dramatest.presentation.searchuser.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 즐겨찾기 프래그먼트
 *
 * Local DB 유저 목록을 보여준다. (화면 진입 시)
 * Local DB 삭제한다. (아이템 목록 클릭 시)
 * Local DB 유저 검색를 검색한다. (키워드 입력 후 검색 버튼 클릭 시)
 */
@AndroidEntryPoint
class BookmarkFragment : BaseFragment() {
    @Inject
    lateinit var searchUserAdapter: SearchUserAdapter
    private lateinit var binding: FragmentBookmarkBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(btnApply) {
                setOnClickListener {
                    // 검색 클릭
                    val keyword = editSearch.text.toString()
                    searchUserAdapter.putItems(viewModel.cacheSearchUsers(keyword))
                }
            }
            with(recyclerView) {
                adapter = searchUserAdapter.apply {
                    // 즐겨찾기 설정
                    setBookmark {
                        with(viewModel) {
                            // 즐겨찾기 삭제, 검색 프래그먼트에 아이디 전달
                            bookmarkLiveData.value = it.id
                            // 어댑터에서 클릭한 유저 데이터 DB 저장하기
                            viewModel.cacheUser(it.isBookmark!!, it)
                        }
                        loadAllUsers()

                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        with(viewModel) {
            // 액티비티에 타이틀 데이터 전달하기
            titleLiveData.value = getString(com.dony.dramatest.R.string.term_bookmark)
            loadAllUsers()
        }
    }

    private fun loadAllUsers() {
        with(viewModel.cacheLoadAllUser()) items@{
            // 저장된 유저 목록 가져오기
            with(binding) {
                if (size == 0) txtEmpty.toVisible() else txtEmpty.toGone()
                searchUserAdapter.putItems(this@items)
            }
        }
    }

    companion object {
        fun newInstance() = BookmarkFragment()
    }
}