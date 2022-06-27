package com.bor2h.siportstest

import com.bor2h.siportstest.databinding.ActivityBoardInsideBinding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bor2h.siportstest.FBRef.Companion.mDatabaseReference
import com.bor2h.siportstest.FBRef.Companion.mDatabaseReference2

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Exception

//
class BoardInsideActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var binding: ActivityBoardInsideBinding
    private lateinit var key: String


    //private var mChildEventListener: ChildEventListener? = null

    // Views
    //private var mListView: ListView? = null

    // Values
    //private var mAdapter: ChatAdapter? = null

    private var message: String = ""

    private var writerUid : String = ""

    private lateinit var btn_send : Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }


        key = intent.getStringExtra("key").toString()
        getBoardData(key)

        initViews()
        //여기부터는 다이어로그
    }

    private fun showDialog() {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            Toast.makeText(this, "수정 버튼을 눌렀습니다", Toast.LENGTH_LONG).show()

            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)


        }

        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {

            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제되었습니다", Toast.LENGTH_LONG).show()
            finish()

        }


    }

    private fun getBoardData(key: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    Log.d("TAG", dataModel!!.title)

                    binding.titleArea.text = dataModel.title
                    binding.textArea.text = dataModel.content
                    binding.timeArea.text = dataModel.time

                    val myUid = FBAuth.getUid()
                    writerUid = dataModel.uid

                    if (myUid.equals(writerUid)) {
                        Log.d("TAG", "내가 쓴 글")
                        binding.boardSettingIcon.isVisible = true
                    } else {
                        Log.d("TAG", "다른사람이 쓴글")
                    }

                } catch (e: Exception) {

                    Log.d("TAG", "삭제완료")

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)


    }

    private fun initViews() {
        findViewById<Button>(R.id.btn_send).setOnClickListener(this)
        //btn_send=findViewById(R.id.sendBtn)
    }

    override fun onClick(v: View) {
/*
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.request_send_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("참여 요청 전송")

        val alertDialog = mBuilder.create()
        alertDialog.show()
*/
        //여기부터는 다이어로그
       // val requestSendBtn=alertDialog.findViewById<Button>(R.id.requestSendBtn)
        message = "참여 요청이 도착했습니다."
        if (message.isNotEmpty()) {
            val chatData = ChatData()
            chatData.userid = FBAuth.getUid()
            chatData.message = message
            chatData.time = System.currentTimeMillis()
            chatData.name = binding.nameTxt.text.toString()
            chatData.userid2 = writerUid
            //"6I5QdVmkyqfoEhxgxqvv0cJKodG2" // qwer 사용자 id -> 게시판 보여주는 창에서 작성자의 id 가져오기
            if (chatData.userid.toString() != chatData.userid2) {
                mDatabaseReference.push().setValue(chatData)
                Toast.makeText(this, "참여 요청을 보냈습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "자기 자신에겐 전송 금지", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
