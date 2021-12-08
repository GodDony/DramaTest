package com.dony.dramatest.presentation.searchuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.dony.dramatest.R
import com.dony.dramatest.base.BaseViewHolder
import com.dony.dramatest.databinding.ListItemHeaderBinding
import com.dony.dramatest.databinding.ListItemUserBinding
import com.dony.dramatest.presentation.model.UserModel
import javax.inject.Inject

/**
 * 요구사항 : Github 사용자 검색 API 응답을 받아 사용자 리스트를
 * 보여줍니다. 아이템 뷰는 프로필 이미지, 사용자 이름, 즐겨찾기
 * 여부를 보여줍니다. 사용자가 이미 즐겨찾기에 추가되어 있으면
 * 이를 표시합니다.
 */
class SearchUserAdapter @Inject constructor() : RecyclerView.Adapter<BaseViewHolder>() {
    private val diffUtil = AsyncListDiffer(this, UserDiffUtil())
    private var bookmarkUnit: ((UserModel) -> Unit)? = null

    fun putItems(items: ArrayList<UserModel>) {
        diffUtil.submitList(items)
    }

    fun removeBookmark(id: Int) {
        diffUtil.currentList.forEachIndexed { index, userModel ->
            if (userModel.id == id) {
                userModel.isBookmark = false
                notifyItemChanged(index)
            }
        }
    }

    fun setBookmark(unit: (UserModel) -> Unit) {
        bookmarkUnit = unit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == VIEW_TYPE_BODY) {
            BaseViewHolder(ListItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            BaseViewHolder(ListItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        with(diffUtil.currentList[holder.adapterPosition]) model@{
            if (viewType == VIEW_TYPE_BODY) {
                // 뷰타입 바디
                with(holder.binding as ListItemUserBinding) {
                    userModel = this@model
                    this@model.isBookmark?.run isBookmark@{
                        imgBookmark.setImageResource(
                            if (this@isBookmark) R.drawable.ic_favorite_activated else R.drawable.ic_favorite_outline
                        )
                    }
                }

                with(holder.itemView) {
                    setOnClickListener {
                        this@model.isBookmark = !this@model.isBookmark!!
                        bookmarkUnit?.invoke(this@model)
                        notifyItemChanged(holder.adapterPosition)
                    }
                }
            } else {
                // 뷰타입 헤더
                (holder.binding as ListItemHeaderBinding).userModel = this
            }
        }
    }

    override fun getItemCount() = diffUtil.currentList.size

    override fun getItemViewType(position: Int): Int {
        return diffUtil.currentList[position].viewType ?: VIEW_TYPE_BODY
    }

    companion object {
        const val VIEW_TYPE_BODY = 0
        const val VIEW_TYPE_HEADER = 1
    }
}