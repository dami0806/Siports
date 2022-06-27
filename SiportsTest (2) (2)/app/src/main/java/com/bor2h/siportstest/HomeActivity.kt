package com.bor2h.siportstest

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bor2h.siportstest.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {

    companion object {
        private const val TAG_HOME = "fragment_home"
        private const val TAG_MAP = "fragment_map"
        private const val TAG_CHAT = "fragment_chat"
        private const val TAG_PROFILE = "fragment_profile"
    }

    // 뷰 바인딩
    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상태 바 색상 변경
        window.statusBarColor = Color.parseColor("#FF45AEEF")

        // 뷰 바인딩
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intentMap = Intent(this, MapActivity::class.java)
        val intentBoard = Intent(this, TalkActivity::class.java)

        // 초기 프래그먼트
        setFragment(TAG_HOME, HomeFragment())

        // 하단 네비게이션 바 항목 클릭 시 프래그먼트 변경 리스너 등록
        binding.bnvHome.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_home -> setFragment(TAG_HOME, HomeFragment())
                R.id.menu_map -> startActivity(intentMap)// setFragment(TAG_MAP, MapActivity())
                R.id.menu_chat -> startActivity(intentBoard)
                R.id.menu_profile -> setFragment(TAG_PROFILE, ProfileFragment())
            }
            true
        }
    }

    // 프래그먼트 설정
    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

        // 트랜잭션에 태그로 전달된 프래그먼트가 없을 경우 추가
        if(manager.findFragmentByTag(tag) == null) {
            ft.add(R.id.frame_layout, fragment, tag)
        }

        // FragmentManager에 추가된 프래그먼트들을 변수로 할당
        val home = manager.findFragmentByTag(TAG_HOME)
//        val map = manager.findFragmentByTag(TAG_MAP)
//        val chat = manager.findFragmentByTag(TAG_CHAT)
        val profile = manager.findFragmentByTag(TAG_PROFILE)

        // 모든 프래그먼트 hide
        if(home != null) {
            ft.hide(home)
        }
//        if(map != null) {
//            ft.hide(map)
//        }
//        if(chat != null) {
//            ft.hide(chat)
//        }
        if(profile != null) {
            ft.hide(profile)
        }

        // 선택한 항목에 해다오디는 프래그먼트만 show
        if(tag == TAG_HOME) {
            if(home != null) {
                ft.show(home)
            }
        }
//        else if(tag == TAG_MAP) {
//            if(map != null) {
//                ft.show(map)
//            }
//        }
//        else if(tag == TAG_CHAT) {
//            if(chat != null) {
//                ft.show(chat)
//            }
//        }
        else if(tag == TAG_PROFILE) {
            if(profile != null) {
                ft.show(profile)
            }
        }

        // 마무리
        ft.commitAllowingStateLoss()
        // ft.commit()
    }
}