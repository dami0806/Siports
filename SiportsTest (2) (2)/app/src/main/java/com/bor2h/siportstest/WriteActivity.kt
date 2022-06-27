package com.bor2h.siportstest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView


class WriteActivity : AppCompatActivity() {
    private lateinit var c1:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)
        c1=findViewById(R.id.category1)
        c1.setOnClickListener {
            var intent = Intent(this, TalkActivity::class.java)
            startActivity(intent)
        }



    }
}