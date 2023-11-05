package presentation.chat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tome_aos.R
import com.example.tome_aos.databinding.ActivityChatBinding
import presentation.chat.Adapter.chatAdapter

class ChatActivity: AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var et: EditText
    private lateinit var ibtn: ImageButton
    private val messageList = mutableListOf<String>()
    private lateinit var adapter: chatAdapter
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val ibtnQuestion = binding.ibtnChatQuestion
        ibtn = binding.ibtnChatSend
        et = binding.etChatTalk
        recyclerView = binding.chatRecycler
        adapter = chatAdapter(messageList)
        recyclerView.adapter = adapter
        //메시지 방향 아래서부터 위로
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        binding.pgbarChat.progress=2
        //설명 버튼 on/off
        ibtnQuestion.setOnClickListener {
            binding.tvChatInst.visibility = if (binding.tvChatInst.visibility == View.VISIBLE)
                View.INVISIBLE else View.VISIBLE
        }
        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                binding.tvChatInst.visibility = View.INVISIBLE
                hideKeyboard()
            }
            false
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
    private fun hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        imm.hideSoftInputFromWindow(et.windowToken, 0)
        et.clearFocus()
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
}