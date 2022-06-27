package com.bor2h.siportstest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


@Suppress("NAME_SHADOWING")
class ChatAdapter(context: Context?, resource: Int) :
    ArrayAdapter<ChatData?>(context!!, resource) {
    private val mSimpleDateFormat = SimpleDateFormat("a h:mm", Locale.getDefault())

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.chat_list_item, null)
            viewHolder = ViewHolder()
            viewHolder.mTxtUserName = convertView!!.findViewById<View>(R.id.txt_userName) as TextView?
            viewHolder.mTxtMessage = convertView.findViewById<View>(R.id.txt_message) as TextView?
            viewHolder.mTxtTime = convertView.findViewById<View>(R.id.txt_time) as TextView?
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        val chatData = getItem(position)
        if(chatData!!.message != "참여 요청이 도착했습니다."){
            viewHolder.mTxtUserName!!.text = "사용자 " + chatData!!.name + " 로 부터 비밀번호를 받았습니다."
            viewHolder.mTxtMessage!!.text = chatData.message
            viewHolder.mTxtTime!!.text = mSimpleDateFormat.format(chatData.time)
        }else{
            viewHolder.mTxtUserName!!.text = "요청 보낸 사용자: " + chatData!!.name
            viewHolder.mTxtMessage!!.text = chatData.message
            viewHolder.mTxtTime!!.text = mSimpleDateFormat.format(chatData.time)
        }
        return convertView
    }

    private inner class ViewHolder {
        var mTxtUserName: TextView? = null
        var mTxtMessage: TextView? = null
        var mTxtTime: TextView? = null
    }
}


class ChatData {
    var firebaseKey // Firebase Realtime Database 에 등록된 Key 값
            : String? = null
    var userid // 송신 사용자 id
            : String? = null
    var message // 작성한 메시지
            : String? = null
    var time // 작성한 시간
            : Long = 0
    var userid2 // 수신 사용자 id
            : String? = null
    var name // 전송시 이름
            : String? = null
}
