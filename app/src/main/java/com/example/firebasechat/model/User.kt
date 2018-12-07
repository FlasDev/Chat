package com.example.firebasechat.model

data class User(
    var uid: String? = null,
    var photo: String? = null,
    var email: String?=null,
    var phone: String?=null,
    var name: String?=null,
    var password: String?=null,
    var gender: String?=null
)