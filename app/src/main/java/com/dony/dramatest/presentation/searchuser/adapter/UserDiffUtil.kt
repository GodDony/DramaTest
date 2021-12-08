package com.dony.dramatest.presentation.searchuser.adapter

import androidx.recyclerview.widget.DiffUtil
import com.dony.dramatest.presentation.model.UserModel

class UserDiffUtil : DiffUtil.ItemCallback<UserModel>() {
    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel) = oldItem == newItem
}