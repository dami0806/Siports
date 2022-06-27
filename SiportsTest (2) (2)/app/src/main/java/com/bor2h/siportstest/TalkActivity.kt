package com.bor2h.siportstest

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import androidx.core.view.get
import com.bor2h.siportstest.databinding.ActivityTalkBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TalkActivity : AppCompatActivity() {

    private lateinit var pencil: ImageView
    private lateinit var notiBtn: Button
    private val boardDataList = mutableListOf<BoardModel>()
    private lateinit var boardRVAdapter : BoardListLVAdapter
    private val boardKeyList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk)

        window.statusBarColor = Color.parseColor("#FF45AEEF")

        pencil = findViewById<ImageView>(R.id.writeBtn)
        notiBtn=findViewById<Button>(R.id.notiBtn)
        pencil.setOnClickListener {
            val intent = Intent(this,BoardWriteMainActivity::class.java)
            startActivity(intent)
        }
        notiBtn.setOnClickListener{
            val intent = Intent(this,NotiListActivity::class.java)
            startActivity(intent)
        }
        //val boardList = mutableListOf<BoardModel>()


        boardRVAdapter = BoardListLVAdapter(boardDataList)
        val board= findViewById<ListView>(R.id.boardListView)
        board.adapter = boardRVAdapter
//누르면 다른 activity intent로 값 전달하기
        board.setOnItemClickListener { parent, view, position, id->


            /*  val intent = Intent(this, BoardInsideActivity::class.java)
              intent.putExtra("title", boardDataList[position].title)
              intent.putExtra("content", boardDataList[position].content)
              intent.putExtra("time", boardDataList[position].time)
  */
            val intent = Intent(this, BoardInsideActivity::class.java)
            intent.putExtra("key", boardKeyList[position])


            startActivity(intent)
        }
        //firebase id별로 데이터 호출

        getFBBoardData()

    }

    private fun getFBBoardData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //firebase data change 중복 제거
                boardDataList.clear()
//한덩어리로
                for (dataModel in dataSnapshot.children) {



                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    //키값 받아오기
                    boardKeyList.add(dataModel.key.toString())

                }
                //최신글 위로
                boardKeyList.reverse()
                boardDataList.reverse()
                boardRVAdapter.notifyDataSetChanged()




            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }
        FBRef.boardRef.addValueEventListener(postListener)

    }


}