package com.example.firebasechat.ui.adapters

import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.media.Image
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.firebasechat.utils.CursorRecyclerViewAdapter
import com.example.firebasechat.R
import com.example.firebasechat.model.Picture
import com.example.firebasechat.utils.GlideApp
import java.io.File

class PictureCursorAdapter(
    private val context: Context,
    private val cursor: Cursor
): CursorRecyclerViewAdapter<PictureCursorAdapter.PictureViewHolder>(context, cursor) {

    var onPictureClickCallback: OnPictureSelectedListener? = null

    private var previousPicture: Picture? = null

    override fun onBindViewHolder(holder: PictureViewHolder, cursor: Cursor) {
        val currentCursorId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
        val currentCursorPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
        val picture = Picture(currentCursorId, currentCursorPath, cursor.position)

        if (previousPicture?.id == picture.id){
            picture.isChecked = true
        }

        if (picture.isChecked){
            holder.imgCheck.visibility = View.VISIBLE
        }else{
            holder.imgCheck.visibility = View.GONE
        }

        val file = File(picture.path)
        if (file.exists()){
            GlideApp.with(context)
                .asBitmap()
                .load(file)
                .centerCrop()
                .override(80, 80)
                .into(holder.imgPicture)
                .waitForLayout()
        }

        holder.imgPicture.setOnClickListener {
            holder.imgCheck.visibility = View.VISIBLE

            if (previousPicture != null){
                if (picture.id == previousPicture?.id){
                    holder.imgCheck.visibility = View.GONE
                    previousPicture?.isChecked = false
                    notifyItemChanged(previousPicture?.positionOnRecyclerView!!)
                    return@setOnClickListener
                }
                if (previousPicture?.isChecked == true){
                    notifyItemChanged(previousPicture?.positionOnRecyclerView!!)
                }
            }
            picture.isChecked = true
            previousPicture = picture

            if (picture.isChecked){
                onPictureClickCallback?.onPictureClick(picture)
            }else{
                onPictureClickCallback?.onPictureClick(null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PictureViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.picture_item, parent, false)
        return PictureViewHolder(view)
    }

    inner class PictureViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imgPicture = itemView.findViewById<ImageView>(R.id.img_picture)
        val imgCheck = itemView.findViewById<ImageView>(R.id.img_picture_check)
    }

    interface OnPictureSelectedListener{
        fun onPictureClick(picture: Picture?)
    }
}