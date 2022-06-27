package com.bor2h.siportstest

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object {

        private val database = Firebase.database


        val boardRef = database.getReference("board")
        val mDatabaseReference = database.getReference("request") // 참여 요청 보냄
        val mDatabaseReference2 = database.getReference("passwd_send") // 오픈톡방 비밀번호 보냄


    }

}