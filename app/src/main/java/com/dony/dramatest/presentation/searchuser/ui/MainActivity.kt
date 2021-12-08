package com.dony.dramatest.presentation.searchuser.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dony.dramatest.R
import com.dony.dramatest.base.BaseActivity
import com.dony.dramatest.databinding.ActivityMainBinding
import com.dony.dramatest.extention.observe
import com.dony.dramatest.presentation.searchuser.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

/**
 * 메인 액티비티
 * ViewPager2 + BottomNavigationView 조합으로 Fragment 화면 관리
 * 하나의 뷰모델로 액티비티와 프래그먼트가 공유하며 사용한다.
 */
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val bookmarkFragment = BookmarkFragment.newInstance()
    private val searchUserFragment = SearchUserFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        with(binding) {
            setSupportActionBar(toolbar)

            // ViewPager2
            with(viewpager) {
                adapter = BottomNavFragmentAdapter()
                offscreenPageLimit = 1
                // non swipe
                isUserInputEnabled = false

                setPageTransformer { view, position ->
                    // 자연스러운 인터렉션을 위해 스크롤 애니메이션 효과 대신 알파 효과 설정
                    view.translationX = view.width * -position
                    if (position <= -1.0F || position >= 1.0F) {
                        view.alpha = 0.0F
                        view.visibility = View.GONE
                    } else if (position == 0.0F) {
                        view.alpha = 1.0F
                        view.visibility = View.VISIBLE
                    } else {
                        // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                        view.alpha = 1.0F - abs(position)
                    }
                }
            }

            // BottomNavigationView
            with(bottomNav) {
                setOnItemSelectedListener {
                    // 바텀 네비게이션 뷰 리스너를 통해 뷰페이저 컨트롤
                    viewpager.currentItem = when (it.itemId) {
                        R.id.menu_search_user -> VIEW_SEARCH_USER
                        R.id.menu_bookmark -> VIEW_BOOKMARK
                        else -> VIEW_SEARCH_USER
                    }
                    true
                }
            }
        }

        with(viewModel) {
            // 구독 설정
            observe(titleLiveData) {
                // 프래그먼트에서 전달 받은 타이틀 설정
                supportActionBar?.title = it
            }
        }
    }

    /**
     * 유저 검색 프래그먼트, 즐겨찾기 프래그먼트를 관리하는 어댑터
     * 바텀 네비게이션 뷰와 페어하다.
     */
    inner class BottomNavFragmentAdapter : FragmentStateAdapter(this) {
        override fun getItemCount() = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> searchUserFragment
                else -> bookmarkFragment
            }
        }
    }

    companion object {
        // 어댑터 프래그먼트 포지션
        const val VIEW_SEARCH_USER = 0
        const val VIEW_BOOKMARK = 1
    }
}