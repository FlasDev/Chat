package com.example.firebasechat.ui


import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.v4.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.firebasechat.R
import com.google.firebase.auth.FirebaseAuth


class OtherFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_other, container, false)
        auth = FirebaseAuth.getInstance()
        val btnSignOut = view.findViewById<MaterialButton>(R.id.btn_other_sign_out)
        btnSignOut.setOnClickListener {
            if (auth.currentUser != null){
                auth.signOut()
                activity?.finish()
                startActivity(LoginActivity.newIntent(context!!))
            }
        }

        return view
    }

    companion object {
        const val PAGE_TITLE = "Other"
        fun newInstance(): Fragment = OtherFragment()
    }

}
