package com.example.firebasechat.ui


import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.SearchView
import com.example.firebasechat.R
import com.example.firebasechat.model.Friend
import com.example.firebasechat.model.User
import com.example.firebasechat.ui.adapters.FriendListAdapter
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference


class ListUserFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageRef: StorageReference
    private lateinit var adapter: FriendListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_list_user, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_listUser)
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/")
        //val arrayList = arrayListOf<Friend>()
        //arrayList.add(Friend("/storage/sdcard1/DCIM/Camera/IMG_20181104_161742_104.JPG","Oleg Chausov"))
        //arrayList.add(Friend("/storage/sdcard1/DCIM/Camera/IMG_20181104_231159.jpg","Desmond"))
       // arrayList.add(Friend("/storage/sdcard1/DCIM/Camera/IMG_20180202_150531.jpg","Picasso"))
        val manager = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        adapter = FriendListAdapter(context!!, arrayListOf())
        recyclerView.addItemDecoration(decoration)
        recyclerView.layoutManager = manager
        recyclerView.setHasFixedSize(true)
        readAllUserFromDatabase()
        recyclerView.adapter = adapter
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.friend_list_menu, menu)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.friend_search)?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        }
    }

    private fun readAllUserFromDatabase(){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val snapshot = dataSnapshot.children
                val userList = arrayListOf<User>()
                for (childSnapshot in snapshot){
                    val user = childSnapshot.getValue(User::class.java)!!
                    user.uid = childSnapshot.key
                    userList.add(user)
                    adapter.setData(userList)
                }
                //Log.d("myLogs", userList.joinToString(", "))
            }
        })


    }

    companion object {
        fun newInstance(): Fragment = ListUserFragment()
    }
}
