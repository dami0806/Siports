package com.bor2h.siportstest

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bor2h.siportstest.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    companion object {
        private var imageUri: Uri? = null
        private val fireStorage = FirebaseStorage.getInstance().reference
        private val fireDatabase = FirebaseDatabase.getInstance().reference
        private val user = Firebase.auth.currentUser
        private val uid = user?.uid.toString()

        private const val PERMISSION_CAMERA = android.Manifest.permission.CAMERA
        private const val PERMISSION_READ_EXTERNAL_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE
        private const val PERMISSION_WRITE_EXTERNAL_STORAGE = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

        private val PERMISSIONS_REQUESTED: Array<String> = arrayOf(
            PERMISSION_CAMERA,
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_WRITE_EXTERNAL_STORAGE)
    }

    // 뷰 바인딩
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                imageUri = result.data?.data // 이미지 경로 원본
                Log.d("ImageURIClient", "$imageUri")

                // 기존 사진을 삭제 후 새로운 사진을 등록
                // fireStorage.child("userImages/$uid/photo").delete().addOnSuccessListener {
                // Log.d("ImageURIServer", "Delete Success")
                    fireStorage.child("userImages/$uid/photo").putFile(imageUri!!)
                        .addOnSuccessListener {
                            Log.d("ImageURI", "Upload Success")
                            fireStorage.child("userImages/$uid/photo").downloadUrl.addOnSuccessListener {
                                val photoUri: Uri = it
                                Log.d("ImageURI", "Download Success $photoUri")
                                fireDatabase.child("users/$uid/profileImageUrl")
                                    .setValue(photoUri.toString())
                            }
                        }
                // }
                Log.d("UpdatePhoto", "Success")
            } else {
                Log.d("UpdatePhoto", "Fail")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // requestPermissions()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // 뷰 바인딩
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        val profileImage = binding.ivProfile

        /* 닉네임 */
        val tvName = binding.tvName
        val etName = binding.etName

        /* 데이터베이스에 국적 저장 */
        val cgNationality = binding.cgNationality
        val cNationalityLocal = binding.cNationalityLocal
        val cNationalityForeign = binding.cNationalityForeign

        /* 데이터베이스에 성별 저장 */
        val cgGender = binding.cgGender
        val cGenderMan = binding.cGenderMan
        val cGenderWoman = binding.cGenderWoman

        /* 데이터베이스에 나이 저장 */
        val cgAge = binding.cgAge
        val cAge10 = binding.cAge10
        val cAge20 = binding.cAge20
        val cAge30 = binding.cAge30
        val cAge40 = binding.cAge40
        val cAge50 = binding.cAge50
        val cAge60 = binding.cAge60


        /* 데이터베이스에 종목 저장 */
        val cgEvent = binding.cgEvent
        val cEventFutsal = binding.cEventFutsal
        val cEventSoccer = binding.cEventSoccer
        val cEventBaseball = binding.cEventBaseball
        val cEventBadminton = binding.cEventBadminton
        val cEventSports = binding.cEventSports

        val clBackground = binding.clBackground

        Log.d("Auth", "$uid")

        /* Snapshot */
        val userProfileListener = object : ValueEventListener {
            override fun onDataChange(Snapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val userProfile = Snapshot.getValue<UserProfile>()

                if (userProfile?.profileImageUrl != null) {
                    Glide.with(requireContext()).load(userProfile?.profileImageUrl)
                        .apply(RequestOptions().circleCrop())
                        .into(profileImage!!)
                }

                tvName.text = userProfile?.name

                when (userProfile?.nationality) {
                    cNationalityLocal.text -> cNationalityLocal.isChecked = true
                    cNationalityForeign.text -> cNationalityForeign.isChecked = true
                }

                when (userProfile?.gender) {
                    cGenderMan.text -> cGenderMan.isChecked = true
                    cGenderWoman.text-> cGenderWoman.isChecked = true
                }

                when (userProfile?.age) {
                    cAge10.text -> cAge10.isChecked = true
                    cAge20.text -> cAge20.isChecked = true
                    cAge30.text -> cAge30.isChecked = true
                    cAge40.text -> cAge40.isChecked = true
                    cAge50.text -> cAge50.isChecked = true
                    cAge60.text -> cAge60.isChecked = true
                }


                when (userProfile?.event) {
                    cEventFutsal.text -> cEventFutsal.isChecked = true
                    cEventSoccer.text -> cEventSoccer.isChecked = true
                    cEventBaseball.text -> cEventBaseball.isChecked = true
                    cEventBadminton.text -> cEventBadminton.isChecked = true
                    cEventSports.text -> cEventSports.isChecked = true
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        fireDatabase.child("users").child(uid).addValueEventListener(userProfileListener)

        // 프로필 사진 변경
        profileImage!!.setOnClickListener {
            val intentImage = Intent(Intent.ACTION_PICK)
            intentImage.type = MediaStore.Images.Media.CONTENT_TYPE
            getContent.launch(intentImage)
        }

        /* 닉네임 */
        tvName.setOnClickListener {
            tvName.visibility = View.INVISIBLE
            etName.visibility = View.VISIBLE
        }

        etName.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                tvName.visibility = View.VISIBLE
                etName.visibility = View.INVISIBLE
                fireDatabase.child("users/$uid/name").setValue(etName.text.toString())
            }
        }

        /* 국적 */
        cgNationality.setOnCheckedChangeListener { chipGroup, checkedId ->
            when (checkedId) {
                cNationalityLocal.id ->
                    fireDatabase.child("users/$uid/nationality")
                        .setValue(cNationalityLocal.text.toString())
                cNationalityForeign.id ->
                    fireDatabase.child("users/$uid/nationality")
                        .setValue(cNationalityForeign.text.toString())
            }
        }

        /* 성별 */
        cgGender.setOnCheckedChangeListener { chipGroup, checkedId ->
            when (checkedId) {
                cGenderMan.id ->
                    fireDatabase.child("users/$uid/gender").setValue(cGenderMan.text.toString())
                cGenderWoman.id ->
                    fireDatabase.child("users/$uid/gender").setValue(cGenderWoman.text.toString())
            }
        }



        /* 나이 */
        cgAge.setOnCheckedChangeListener { chipGroup, checkedId ->
            when (checkedId) {
                cAge10.id ->
                    fireDatabase.child("users/$uid/age").setValue(cAge10.text.toString())
                cAge20.id ->
                    fireDatabase.child("users/$uid/age").setValue(cAge20.text.toString())
                cAge30.id ->
                    fireDatabase.child("users/$uid/age").setValue(cAge30.text.toString())
                cAge40.id ->
                    fireDatabase.child("users/$uid/age").setValue(cAge40.text.toString())
                cAge50.id ->
                    fireDatabase.child("users/$uid/age").setValue(cAge50.text.toString())
                cAge60.id ->
                    fireDatabase.child("users/$uid/age").setValue(cAge60.text.toString())
            }
        }


        /* 종목 */
        cgEvent.setOnCheckedChangeListener { chipGroup, checkedId ->
            when (checkedId) {
                cEventFutsal.id ->
                    fireDatabase.child("users/$uid/event").setValue(cEventFutsal.text.toString())
                cEventSoccer.id ->
                    fireDatabase.child("users/$uid/event").setValue(cEventSoccer.text.toString())
                cEventBaseball.id ->
                    fireDatabase.child("users/$uid/event").setValue(cEventBaseball.text.toString())
                cEventBadminton.id ->
                    fireDatabase.child("users/$uid/event").setValue(cEventBadminton.text.toString())
                cEventSports.id ->
                    fireDatabase.child("users/$uid/event").setValue(cEventSports.text.toString())
            }
        }

        clBackground.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                hideKeyBoard()
            }
        }

        return view
    }

    // 프래그먼트에서 뷰 바인딩으로 인한 메모리 누수 방지
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun requestPermissions() {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            Log.d("권한요청", "$it")
        }.launch(PERMISSIONS_REQUESTED)
    }

    private fun hideKeyBoard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }
}