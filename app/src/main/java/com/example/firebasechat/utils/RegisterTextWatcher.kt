package com.example.firebasechat.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.rengwuxian.materialedittext.MaterialEditText

abstract class RegisterTextWatcher(private val editText: MaterialEditText): TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        if (validate(s?.toString()!!)){
            editText.error = errorMessage()
        }else{
            editText.error = null
        }

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    abstract fun validate(text: String): Boolean
    abstract fun errorMessage(): String
}