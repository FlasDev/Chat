package com.example.firebasechat.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.example.firebasechat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Users")

        if (auth.currentUser != null){
            startActivity(MainActivity.newIntent(this))
            finish()
        }

        initUI()
    }

    override fun onStart() {
        super.onStart()
        closeKeyboard()
    }

    private fun initUI() {
        btn_enter.setOnClickListener {view->
            closeKeyboard()
            if (login_email.text?.isNotEmpty()!! && login_password.text?.isNotEmpty()!!) {
                progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Загрузка")
                progressDialog.setMessage("Ожидайте")

                progressDialog.show()
                auth.signInWithEmailAndPassword(login_email.text.toString(), login_password.text.toString())
                    .addOnSuccessListener { authResult ->
                        progressDialog.dismiss()
                        startActivity(MainActivity.newIntent(this))
                        finish()
                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Snackbar.make(login_layout, "Failed. ${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
            }
        }

        btn_registration.setOnClickListener {
            startActivityForResult(RegistrationActivity.newIntent(this), REQUEST_CODE)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (login_email.text?.isNotEmpty()!!){
            outState?.putString(STATE_EMAIL, login_email.text.toString())
        }
        if (login_password.text?.isEmpty()!!){
            outState?.putString(STATE_PASSWORD, login_password.text.toString())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.getString(EMAIL)?.also {email->
            login_email.setText(email)
        }
        savedInstanceState?.getString(PASSWORD)?.also {password->
            login_password.setText(password)
        }
    }

    private fun closeKeyboard(){
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                val email = data?.getStringExtra(EMAIL)
                val password = data?.getStringExtra(PASSWORD)
                login_email.setText(email)
                login_password.setText(password)
                Snackbar.make(login_layout, "Почта $email успешно зарегистирована", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val STATE_EMAIL = "userEmail"
        const val STATE_PASSWORD = "userPassword"
        const val EMAIL = "extra_email"
        const val PASSWORD = "extra_password"
        const val REQUEST_CODE = 1

        fun newIntent(packageContext: Context) =
                Intent(packageContext, OtherFragment::class.java)
    }
}
