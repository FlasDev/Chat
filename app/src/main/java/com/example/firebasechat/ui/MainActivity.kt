package com.example.firebasechat.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.firebasechat.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)
        main_navigation.setOnNavigationItemSelectedListener(onNavigationItemSelected)
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, HomeFragment.newInstance())
            .commit()
    }

    val onNavigationItemSelected = BottomNavigationView.OnNavigationItemSelectedListener {
        return@OnNavigationItemSelectedListener when(it.itemId){
            R.id.navigation_home->{
                replaceFragment(HomeFragment.newInstance(), FRAGMENT_HOME)
                true
            }
            R.id.navigation_chat->{

                replaceFragment(ChatFragment.newInstance(), FRAGMENT_CHAT)
                true
            }
            R.id.navigation_friends->{
                replaceFragment(ListUserFragment.newInstance(), FRAGMENT_FRIENDS)
                true
            }
            else -> false
        }
    }

    private fun replaceFragment(newFragment: Fragment, tag: String){
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, newFragment, tag)
            .commit()
    }

    companion object {
        const val FRAGMENT_HOME = "fragmentHome"
        const val FRAGMENT_CHAT = "fragmentChat"
        const val FRAGMENT_FRIENDS = "fragmentFriends"
        fun newIntent(packageContext: Context) =
                Intent(packageContext, MainActivity::class.java)
    }
}
