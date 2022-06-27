package com.bor2h.siportstest

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.bor2h.siportstest.FBRef.Companion.mDatabaseReference
import com.bor2h.siportstest.FBRef.Companion.mDatabaseReference2
import com.bor2h.siportstest.databinding.ActivityNotiListBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class NotiListActivity : AppCompatActivity() {

    // Firebase
    private var mChildEventListener: ChildEventListener? = null

    // Views
    private var mListView: ListView? = null

    // Values
    private var mAdapter: ChatAdapter? = null

    private var message: String = ""
    private var name:String = ""



    private lateinit var binding: ActivityNotiListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.parseColor("#FF45AEEF")
        binding = ActivityNotiListBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_noti_list)



        initViews()
        initFirebaseDatabase()

        mListView!!.setOnItemClickListener { _, _, position: Int, _ ->
            val msg = mAdapter!!.getItem(position)!!.message
            val senderID = mAdapter!!.getItem(position)!!.userid
            if (msg == "참여 요청이 도착했습니다.") {
                showPasswdSendDialog(senderID!!)
            } else {
                showPasswdTextDialog(msg!!)
            }
        }
    }



    private fun showPasswdSendDialog(id : String){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.send_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("오픈채팅방 비밀번호 전송")

        val alertDialog = mBuilder.create()
        alertDialog.show()
        alertDialog.findViewById<Button>(R.id.requestSendBtn)?.setOnClickListener {
            message = alertDialog.findViewById<EditText>(R.id.editPasswd)!!.text.toString()
            name=alertDialog.findViewById<EditText>(R.id.editName2)!!.text.toString()
            if (message.isNotEmpty()) {
                alertDialog.findViewById<EditText>(R.id.editPasswd)!!.setText("")
                val chatData = ChatData()
                chatData.userid = FBAuth.getUid()
                chatData.message = message
                chatData.time = System.currentTimeMillis()
                chatData.name = name
                chatData.userid2 = id //"5bnurOmu9uhh3yPaLYZnlAb5RTb2" // smwoo529 사용자 id -> 요청 보낸 사람의 id 가져오기
                mDatabaseReference2!!.push().setValue(chatData)
                Toast.makeText(this,"오픈채팅방 비밀번호를 보냈습니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            alertDialog.dismiss()
        }
    }

    private fun showPasswdTextDialog(msg:String){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.show_passwd_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("오픈채팅방 비밀번호")

        val alertDialog = mBuilder.create()
        alertDialog.show()
        alertDialog.findViewById<TextView>(R.id.passwd)!!.setText(msg)
    }

    private fun initViews() {
        mListView = findViewById<View>(R.id.list_message) as ListView
        mAdapter = ChatAdapter(this, 0)
        mListView!!.adapter = mAdapter
    }

    private fun initFirebaseDatabase() {
        //mFirebaseDatabase = FirebaseDatabase.getInstance()

        mChildEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val chatData = dataSnapshot.getValue(ChatData::class.java)
                chatData!!.firebaseKey = dataSnapshot.key
                if(chatData.userid2 == FBAuth.getUid()) {
                    mAdapter!!.add(chatData)
                    mListView!!.smoothScrollToPosition(mAdapter!!.count)
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val firebaseKey = dataSnapshot.key
                val count = mAdapter!!.count
                for (i in 0 until count) {
                    if (mAdapter!!.getItem(i)!!.firebaseKey == firebaseKey) {
                        mAdapter!!.remove(mAdapter!!.getItem(i))
                        break
                    }
                }
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        mDatabaseReference.addChildEventListener(mChildEventListener!!)
        mDatabaseReference2.addChildEventListener(mChildEventListener!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDatabaseReference.removeEventListener(mChildEventListener!!)
        mDatabaseReference2.removeEventListener(mChildEventListener!!)
    }
}