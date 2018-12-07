package com.example.firebasechat.ui


import android.os.Bundle
import android.support.v4.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.firebasechat.R


class UserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }


    companion object {

        const val ARG_ID = "id"

        fun newInstance(uid: String): Fragment{
            val userFragment = UserFragment()
            val args = Bundle()
            args.putString(ARG_ID, uid)
            userFragment.arguments = args
            return userFragment
        }
    }

}
