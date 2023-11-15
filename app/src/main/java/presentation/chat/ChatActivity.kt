package presentation.chat

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tome_aos.R
import com.example.tome_aos.databinding.ActivityChatBinding
import presentation.chat.Adapter.ChatAdapter
import util.hideKeyboard


class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var et: EditText
    private lateinit var ibtn: ImageButton
    private val messageList = mutableListOf<String>()
    private lateinit var adapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)

        val ibtnQuestion = binding.ibtnChatQuestion
        var missionProgress = 3

        ibtn = binding.ibtnChatSend
        et = binding.etChatTalk
        recyclerView = binding.chatRecycler
        adapter = ChatAdapter(messageList)
        recyclerView.adapter = adapter

        //메시지 방향 아래서부터 위로
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
            recyclerView.layoutManager = layoutManager
        //설명 버튼 on/off
        ibtnQuestion.setOnClickListener {
            binding.tvChatInst.visibility = if (binding.tvChatInst.visibility == View.VISIBLE)
                View.INVISIBLE else View.VISIBLE
        }
        ibtn.setOnClickListener {
            sendMessage()
        }
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateButtonUI()
            }

            override fun afterTextChanged(s: Editable?) {
                updateButtonUI()
            }
        })

        binding.pgbarChat.progress = missionProgress
        //미션 개수에 따라 알림 표시
        if (missionProgress > 0) {
            binding.chatMissionAlert.visibility = View.VISIBLE
            binding.chatMissionAlert.text = "$missionProgress"
            binding.tvMissionAdded.visibility = View.INVISIBLE
            if (missionProgress == 3) {
                binding.tvMissionAdded.visibility = View.VISIBLE
            }
        } else {
            binding.chatMissionAlert.visibility = View.INVISIBLE
            binding.tvMissionAdded.visibility = View.INVISIBLE
            binding.chatMissionAlert.text = "$missionProgress"
        }

        binding.btnFinishChat.setOnClickListener {
            finish()
        }
        setContentView(binding.root)
    }

    //text watcher로 전송 버튼 변경
    private fun updateButtonUI() {
        var textInput = et.text.toString()
        if (textInput.isEmpty()) {
            ibtn.setImageResource(R.drawable.ic_chat_send_dis)
            ibtn.isEnabled = false
        } else {
            ibtn.setImageResource(R.drawable.ic_chat_send)
            ibtn.isEnabled = true
        }
    }
    //메시지 전송
    private fun sendMessage() {
        val message = et.text.toString()
        if (message.isNotEmpty()) {
            messageList.add(message)
            adapter.notifyItemInserted(messageList.size - 1)
            et.text.clear()
            recyclerView.scrollToPosition(messageList.size - 1)
        }
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev!!.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                hideKeyboard(focusView)
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}