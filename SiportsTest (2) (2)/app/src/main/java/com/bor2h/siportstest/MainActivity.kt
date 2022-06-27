package com.bor2h.siportstest

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.bor2h.siportstest.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {
    // 전역으로 사용할 Firebase Auth를 만들어 준다
    private lateinit var auth : FirebaseAuth

    // 뷰 바인딩
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getHashKey()

        // 상태 바 색상 변경
        window.statusBarColor = Color.parseColor("#FF45AEEF")

        // 파이어베이스 Authentication
        auth = Firebase.auth

        // 뷰 바인딩
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 인텐트
        val intentJoin = Intent(this, JoinActivity::class.java)
        val intentHome = Intent(this, HomeActivity::class.java)
//        val intentMap = Intent(this, MapActivity::class.java)

        binding.btnJoin.setOnClickListener {
            // 회원가입 화면으로 전환
            startActivity(intentJoin)
        }
//        binding.btnMap.setOnClickListener {
//            // 회원가입 화면으로 전환
//            startActivity(intentJoin)
//        }

        initLoginButton(intentHome)
    }

    // 로그인
    private fun initLoginButton(intentHome: Intent) {
        binding.btnLogin.setOnClickListener {
            val email = binding.textFieldEmail.text.toString()
            val password = binding.textFieldPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
//                        Toast.makeText(this,"로그인에 성공했습니다!",Toast.LENGTH_SHORT).show()
                        // 홈 화면으로 전환
                        startActivity(intentHome)
                    } else {
                        Toast.makeText(this,"아이디와 비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    fun getHashKey(){
        var packageInfo : PackageInfo = PackageInfo()
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }

        for (signature: Signature in packageInfo.signatures){
            try{
                var md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KEY_HASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch(e: NoSuchAlgorithmException){
                Log.e("KEY_HASH", "Unable to get MessageDigest. signature = " + signature, e)
            }
        }
    }

}