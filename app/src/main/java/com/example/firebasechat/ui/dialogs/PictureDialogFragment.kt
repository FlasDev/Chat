package com.example.firebasechat.ui.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.example.firebasechat.R
import com.example.firebasechat.model.Picture
import com.example.firebasechat.ui.adapters.PictureCursorAdapter
import java.io.File

class PictureDialogFragment: DialogFragment() {

    private var cursor: Cursor? = null
    private var currentPicture: Picture? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        cursor = context?.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_ADDED),
            "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ?",
            arrayOf("Camera"),
            "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        )

        val manager = GridLayoutManager(context, 2).also {
            it.orientation = RecyclerView.HORIZONTAL
        }
        val pictureClick = object : PictureCursorAdapter.OnPictureSelectedListener{
            override fun onPictureClick(picture: Picture?) {
                currentPicture = picture
            }
        }

        val pictureAdapter = PictureCursorAdapter(context!!, cursor!!).also {
            it.onPictureClickCallback = pictureClick
        }

        val recyclerView = RecyclerView(context!!).apply {
            layoutManager = manager
            adapter = pictureAdapter
        }

        return AlertDialog.Builder(context!!)
            .setView(recyclerView)
            .setIcon(R.drawable.profile_image)
            .setTitle("Загрузка фотки")
            .setPositiveButton("Ok") { dialog, which ->
                if (currentPicture != null){
                    val intent = Intent().apply {
                        putExtra(TAG_PICTURE_SELECTED, currentPicture?.path)
                    }
                    setResultToTargetFragment(intent)
                }else{
                    setResultToTargetFragment(Intent())
                    Toast.makeText(context, "Choose your photo", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Cancle") { dialog, which ->
                dismiss()
            }
            .create()
    }

    private fun setResultToTargetFragment(intent: Intent){
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    override fun onPause() {
        super.onPause()
        setResultToTargetFragment(Intent())
    }

    override fun onDestroy() {
        super.onDestroy()
        cursor?.close()
    }

    companion object {
        const val TAG_PICTURE_SELECTED = "picture"
        fun newInstance(): DialogFragment = PictureDialogFragment()
    }
}