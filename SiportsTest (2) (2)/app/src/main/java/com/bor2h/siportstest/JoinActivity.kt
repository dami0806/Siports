package com.bor2h.siportstest

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.bor2h.siportstest.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {
    // 전역으로 사용할 FirebaseAuth를 만들어 준다
    private lateinit var auth : FirebaseAuth

    // 뷰 바인딩
    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상태 바 색상 변경
        window.statusBarColor = Color.parseColor("#FF45AEEF")

        // 파이어베이스 Authentication
        auth = Firebase.auth

        // 뷰 바인딩
        binding = ActivityJoinBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initSignupButton()
        detectTextFieldEmpty()
    }

    // 버튼을 눌러 회원가입 기능 구현
    private fun initSignupButton() {
        binding.btnJoin.setOnClickListener {
            val email = binding.textFieldEmail.text.toString()
            val password = binding.textFieldPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Toast.makeText(this,"회원가입에 성공했습니다!",Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this,"이미 존재하는 계정이거나, 회원가입에 실패했습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    // 빈 텍스트가 존재하지 않고 비밀번호 재확인 성공 시 버튼 활성화
    // * 비밀번호 재확인 성공 기능 구현 적합 여부 *
    private fun detectTextFieldEmpty() {
        // when Activity Start
        binding.btnJoin.isEnabled = false

        binding.textFieldEmail.addTextChangedListener {
            val email = binding.textFieldEmail.text.toString()
            val password = binding.textFieldPassword.text.toString()
            val passwordCheck = binding.textFieldPasswordCheck.text.toString()
            var enalbed = email.isNotEmpty() && password.isNotEmpty() && passwordCheck.isNotEmpty() && (password == passwordCheck)
            binding.btnJoin.isEnabled = enalbed
        }

        binding.textFieldPassword.addTextChangedListener {
            val email = binding.textFieldEmail.text.toString()
            val password = binding.textFieldPassword.text.toString()
            val passwordCheck = binding.textFieldPasswordCheck.text.toString()
            var enalbed = email.isNotEmpty() && password.isNotEmpty() && passwordCheck.isNotEmpty() && (password == passwordCheck)
            binding.btnJoin.isEnabled = enalbed
        }

        binding.textFieldPasswordCheck.addTextChangedListener {
            val email = binding.textFieldEmail.text.toString()
            val password = binding.textFieldPassword.text.toString()
            val passwordCheck = binding.textFieldPasswordCheck.text.toString()
            var enalbed = email.isNotEmpty() && password.isNotEmpty() && passwordCheck.isNotEmpty() && (password == passwordCheck)
            binding.btnJoin.isEnabled = enalbed
        }
    }
}