package com.example.firebasechat.ui.adapters

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.firebasechat.model.User
import com.example.firebasechat.R
import com.example.firebasechat.utils.UserDiffUtilCallback

class ListUserAdapter(
    private val context: Context
): RecyclerView.Adapter<ListUserAdapter.UserViewHolder>() {

    var onUserClick: OnUserClickListener? = null
    private var listUser = arrayListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UserViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = listUser[position]
        holder.bindUser(user)
    }

    fun setData(listUser: List<User>){
        val userDiffUtilCallback = UserDiffUtilCallback(this.listUser, listUser)
        val userDiffResult = DiffUtil.calculateDiff(userDiffUtilCallback)
        this.listUser.clear()
        this.listUser.addAll(listUser)
        userDiffResult.dispatchUpdatesTo(this)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val txtUserName = itemView.findViewById<TextView>(R.id.user_name)
        fun bindUser(user: User){
            Log.d("myLogs", "bind position = $position")
            txtUserName.text = user.name
            itemView.setOnClickListener {
                onUserClick?.onUserClick(user)
            }
        }
    }

    interface OnUserClickListener{
        fun onUserClick(user: User)
    }
}