package com.example.firebasechat.ui


import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.TextView

import com.example.firebasechat.R
import com.example.firebasechat.ui.dialogs.HomePhotoDialogFragment
import com.example.firebasechat.ui.dialogs.PictureDialogFragment
import com.example.firebasechat.utils.GlideApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

//
class HomeFragment : Fragment() {

    private lateinit var storageRef: StorageReference
    private lateinit var storageDatabase: FirebaseStorage
    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var imgProfilePhoto: CircleImageView
    private lateinit var imgPhotoLibrary: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        activity?.findViewById<AppBarLayout>(R.id.main_appbar)?.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initUI(view)
        ref = FirebaseDatabase.getInstance().getReference("Users")
        auth = FirebaseAuth.getInstance()
        storageDatabase = FirebaseStorage.getInstance()
        return view
    }

    private fun initUI(view: View) {
        view.findViewById<ViewPager>(R.id.home_view_pager).also {
            initViewPager(it)
        }
        view.findViewById<TextView>(R.id.txt_home_name).apply {
            text = "Oleg Chausov"
        }
        imgPhotoLibrary = view.findViewById<ImageButton>(R.id.img_add_photo)
        imgPhotoLibrary.setOnClickListener {
            runPhotoAnim(R.drawable.anim_photo_to_library)
            showPictureFragment()
        }

        imgProfilePhoto = view.findViewById(R.id.img_home_photo)

        imgProfilePhoto.setOnClickListener{
            showPhotoFragment()
        }
    }


    private fun runPhotoAnim(drawable: Int){
        val draw = imgPhotoLibrary.drawable

        if (draw is Animatable){
            val anim = (draw as Animatable)
            anim.start()

            Thread{
                while (!Thread.currentThread().isInterrupted){
                    if (!anim.isRunning){
                        activity?.runOnUiThread {
                            imgPhotoLibrary.setImageResource(drawable)
                        }
                        Thread.currentThread().interrupt()
                    }
                }
            }.start()
        }
    }

    private fun loadImageFrom(filePath: String){
        Log.d("myLogs", filePath)
        val file = File(filePath)
        val fileUri = Uri.fromFile(File(filePath))
        storageRef = storageDatabase.getReference("picture/${auth.currentUser?.uid}/${fileUri.lastPathSegment}")
        storageRef.putFile(fileUri).addOnSuccessListener {
            if (file.exists()){
                GlideApp.with(this)
                    .asBitmap()
                    .load(file)
                    .centerCrop()
                    .into(imgProfilePhoto)
            }
        }


    }
    private fun showPictureFragment(){
        val fragment = PictureDialogFragment.newInstance()
        fragment.setTargetFragment(this, REQUEST_SET_PICTURE)
        fragment.show(fragmentManager, fragment.javaClass.name)
    }

    private fun showPhotoFragment(){
        val bundle = Bundle().apply {
            putString(HomePhotoDialogFragment.ARG_PHOTO_PATH, "/storage/sdcard1/DCIM/Camera/IMG_20181104_161742_104.JPG")
        }
        val fragment = HomePhotoDialogFragment.newInstance().apply {
            arguments = bundle
        }
        fragment.show(fragmentManager, fragment.javaClass.name)
    }

    private fun initViewPager(viewPager: ViewPager) {
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPager.adapter = viewPagerAdapter
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.home_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.findViewById<AppBarLayout>(R.id.main_appbar)?.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        runPhotoAnim(R.drawable.anim_library_to_photo)
        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_SET_PICTURE->{
                    data?.getStringExtra(PictureDialogFragment.TAG_PICTURE_SELECTED)?.also {path->
                        loadImageFrom(path)

                    }
                }
            }
        }
    }

    companion object {
        private const val REQUEST_SET_PICTURE = 1
        fun newInstance(): Fragment = HomeFragment()
        internal class ViewPagerAdapter(fm: FragmentManager?): FragmentStatePagerAdapter(fm) {

            override fun getItem(position: Int): Fragment {
                return if (position == 0){
                    ProfileFragment.newInstance()
                }else{
                    OtherFragment.newInstance()
                }
            }

            override fun getCount(): Int = NUM_ITEMS

            override fun getPageTitle(position: Int): CharSequence? {
                return if (position == 0){
                    ProfileFragment.PAGE_TITLE
                }else{
                    OtherFragment.PAGE_TITLE
                }
            }

            companion object {
                private const val NUM_ITEMS = 2
            }

        }
    }


}
