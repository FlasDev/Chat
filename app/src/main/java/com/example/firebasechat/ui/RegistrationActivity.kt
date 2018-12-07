package com.example.firebasechat.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.firebasechat.R
import com.example.firebasechat.model.User
import com.example.firebasechat.utils.RegisterTextWatcher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        setSupportActionBar(registration_toolbar)
        supportActionBar?.apply {
            title = "Registration"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Users")

        initUI()
    }

    private fun initUI() {

        val listEditText = arrayListOf<MaterialEditText>(registration_email, registration_phone_number, registration_password, registration_name)
        registration_email.addTextChangedListener(object : RegisterTextWatcher(registration_email){
            override fun errorMessage(): String = "Invalid Email"

            override fun validate(text: String): Boolean {
                return text.length < 6
            }
        })

        registration_btn.setOnClickListener {
            var canRegister = false
            for (i in 0 until listEditText.size){
                val currentEditText = listEditText[i]
                if (currentEditText.error == null && !TextUtils.isEmpty(currentEditText.text.toString())){
                    canRegister = true
                }else{
                    canRegister = false
                    break
                }
            }

            canRegister = if (registration_rg.checkedRadioButtonId != -1){
                canRegister
            }else{
                false
            }

            if (canRegister){
                val emailUser = registration_email.text.toString()
                val phone = registration_phone_number.text.toString()
                val name = registration_name.text.toString()
                val password = registration_password.text.toString()

                val gender = if (registration_rg.checkedRadioButtonId == 1){
                    "Мужский"
                }else{
                    "Женский"
                }
                val user = User(email = emailUser, phone = phone, name = name, password = password, gender = gender)
                registerUser(user)
            }
        }
    }

    private fun registerUser(user: User) {
        auth.createUserWithEmailAndPassword(user.email!!, user.password!!)
            .addOnSuccessListener {authResult->
                users.child(FirebaseAuth.getInstance().currentUser?.uid!!)
                    .setValue(user)
                    .addOnSuccessListener {
                        val intent = Intent().apply {
                            putExtra(LoginActivity.EMAIL, user.email)
                            putExtra(LoginActivity.PASSWORD, user.password)
                        }
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Snackbar.make(registration_layout, "Failed. ${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Snackbar.make(registration_layout, "Failed. ${it.message}", Snackbar.LENGTH_SHORT).show()
            }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home->{
                onBackPressed()
            }
        }
        return true
    }

    companion object {
        fun newIntent(packageContext: Context):Intent =
                Intent(packageContext, RegistrationActivity::class.java)
    }
}
