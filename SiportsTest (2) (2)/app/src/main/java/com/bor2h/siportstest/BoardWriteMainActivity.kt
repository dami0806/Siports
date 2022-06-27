package com.bor2h.siportstest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bor2h.siportstest.databinding.ActivityBoardWriteMainBinding

class BoardWriteMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardWriteMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write_main)

        binding.writeBtn.setOnClickListener {

            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()
            Log.d("태크",title)
            Log.d("태크",content)

            FBRef.boardRef
                .push()
                .setValue(BoardModel(title, content, uid, time))
            Toast.makeText(this,"게시글 등록 완료",Toast.LENGTH_SHORT).show()
            finish()





        }
    }
}