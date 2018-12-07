package com.example.firebasechat.ui.adapters

import android.animation.LayoutTransition
import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.firebasechat.R
import com.example.firebasechat.model.Friend
import com.example.firebasechat.model.User
import com.example.firebasechat.utils.GlideApp
import com.example.firebasechat.utils.UserDiffUtilCallback
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import java.io.File

class FriendListAdapter(
    private val context: Context,
    private var list: ArrayList<User>
): RecyclerView.Adapter<FriendListAdapter.FriendViewHolder>() {

    private lateinit var storegReference: StorageReference

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): FriendViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.friend_item, parent, false)
        storegReference = FirebaseStorage.getInstance().getReference("picture")
        return FriendViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {

        val friend = list[position]
        Log.d("myLogs", "onBindViewHolder")
        holder.txtNameFriend.text = friend.name
        val file = File(friend.phone)
        GlideApp.with(context)
            .asBitmap()
            .load(file)
            .into(holder.imgPhotoFriend)

        holder.cardLayout.setOnClickListener {
            holder.btnAddFriend.visibility = if (holder.btnAddFriend.visibility == View.GONE) View.VISIBLE else View.GONE
        }
    }

    fun setData(listFriend: ArrayList<User>){
        this.list = listFriend
        notifyDataSetChanged()
    }

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtNameFriend = itemView.findViewById<TextView>(R.id.txt_friend_name)
        val imgPhotoFriend = itemView.findViewById<CircleImageView>(R.id.img_friend_photo)
        val cardLayout = itemView.findViewById<CardView>(R.id.layout_friend_card)
        val btnAddFriend = itemView.findViewById<Button>(R.id.btn_add_friend)
        val layoutTransition = (itemView as CardView).layoutTransition.apply {
            enableTransitionType(LayoutTransition.CHANGING)
        }
    }
}