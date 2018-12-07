package com.example.firebasechat.ui.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.firebasechat.R
import java.io.File

class HomePhotoDialogFragment: DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyHomePhotoDialogFragment)
    }

    override fun onStart() {
        super.onStart()

        val dialog = this.dialog

        if (dialog != null){
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            //dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.home_photo_layout, container, false)
        val imgPhoto = view.findViewById<ImageView>(R.id.img_pick_home_photo)
        Glide.with(context!!)
            .load(File("/storage/sdcard1/DCIM/Camera/IMG_20181115_105259_013.JPG"))
            .into(imgPhoto)

        return view
    }

    companion object {
        const val ARG_PHOTO_PATH = "photo_path"
        fun newInstance(): DialogFragment = HomePhotoDialogFragment()
    }
}