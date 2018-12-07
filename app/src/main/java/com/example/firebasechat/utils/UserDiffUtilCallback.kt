package com.example.firebasechat.utils

import android.support.v7.util.DiffUtil
import com.example.firebasechat.model.Friend
import com.example.firebasechat.model.User

class UserDiffUtilCallback(
    private val oldList: List<User>,
    private val newList: List<User>
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]

        return oldUser.uid == newUser.uid
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]

        return oldUser.email?.equals(newUser.email)!! &&
                oldUser.phone?.equals(newUser.phone)!! &&
                oldUser.name?.equals(newUser.name)!!
    }
}