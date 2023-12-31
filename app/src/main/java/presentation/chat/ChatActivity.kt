package presentation.chat

import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import application.ApplicationClass
import com.example.tome_aos.R
import com.example.tome_aos.databinding.ActivityChatBinding
import com.example.tome_aos.databinding.ItemSendMessageBinding
import com.google.gson.GsonBuilder
import data.dto.ChatDTO
import data.dto.request.ChatRequest
import data.dto.response.ChatResponse
import data.service.ApiClient
import data.service.ChatService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import presentation.chat.adapter.ChatAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import util.hideKeyboard

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var bindingChat: ItemSendMessageBinding
    private lateinit var et: EditText
    private lateinit var ibtn: ImageButton
    private val messageList = mutableListOf<ChatDTO>()
    private lateinit var adapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        et = binding.etChatTalk
        ibtn = binding.ibtnChatSend

        recyclerView = binding.chatRecycler
        adapter = ChatAdapter(messageList)
        recyclerView.adapter = adapter
        //메시지 방향 아래서부터 위로
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager

        init()
        buttonListener()
    }
    //text watcher로 전송 버튼 변경
    private fun updateButtonUI() {
        val textInput = et.text.toString()
        val isEnabled = textInput.isNotEmpty()
        ibtn.setImageResource(
            if (isEnabled) R.drawable.ic_chat_send
            else R.drawable.ic_chat_send_dis)
        ibtn.isEnabled = isEnabled
    }
    private fun sendMessage() {
        val message = et.text.toString()
        if (message.isNotEmpty()) {
            messageList.add(ChatDTO("SENDER", message))
            adapter.notifyItemInserted(messageList.size - 1)
            et.text.clear()
            recyclerView.scrollToPosition(messageList.size - 1)
            postMessage(message)
        }
    }
    //메시지 반환
    private fun receiveMessage(response: String) {
        messageList.add(ChatDTO("RECEIVER", response))
        adapter.notifyItemInserted(messageList.size - 1)
        recyclerView.scrollToPosition(messageList.size - 1)
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
    private fun buttonListener() {
        //message 전송
        ibtn.setOnClickListener {
            sendMessage()
        }
        //설명 버튼 on/off
        binding.ibtnChatQuestion.setOnClickListener {
            binding.tvChatInst.visibility = if (binding.tvChatInst.visibility == View.VISIBLE)
                View.INVISIBLE else View.VISIBLE
        }
        //채팅종료
        binding.btnFinishChat.setOnClickListener {
            finish()
        }
    }
    private fun postMessage(content: String?) {
        //입력 제한
        disableEditText()
        val client = ApiClient.getApiClient().create(ChatService::class.java)
        lifecycleScope.launch {
            val accessToken = ApplicationClass.getInstance().getDataStore().accessToken.first()
            val refreshToken = ApplicationClass.getInstance().getDataStore().refreshToken.first()
            val chatRequest = ChatRequest(content)
            val requestBody = GsonBuilder()
                .serializeNulls().create()
                .toJson(chatRequest)
                .toRequestBody("application/json".toMediaTypeOrNull())
            client.postMessage(accessToken, refreshToken, requestBody).enqueue(object : Callback<ChatResponse> {
                override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                    enableEditText()
                    if (response.isSuccessful) {
                        val chatResponse = response.body()?.message.toString()
                        val mission = response.body()!!.mission_count
                        if (response.body()?.message != null) {
                            receiveMessage(chatResponse)
                        }
                        println("미션 개수: $mission")
                        updateMission(mission)
                    } else {
                        println("HTTP 오류: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    enableEditText()
                    //t.printStackTrace()
                    Toast.makeText(this@ChatActivity, "서버 통신 실패 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    //println("통신 실패")
                }
            })
        }
    }
    private fun disableEditText() {
        binding.lottieBubble.visibility = View.VISIBLE
        binding.etChatTalk.hint = ""
        et.isEnabled = false // EditText 비활성화
        ibtn.isEnabled = false // 전송 버튼도 비활성화
    }
    private fun enableEditText() {
        binding.lottieBubble.visibility = View.GONE
        binding.etChatTalk.hint = "티오에게 메시지 보내기"
        et.isEnabled = true // EditText 활성화
        ibtn.isEnabled = true // 전송 버튼도 활성화
    }
    private fun updateMission(mission: Int) {
        if (mission > 0) {
            binding.chatMissionAlert.visibility = View.VISIBLE
            binding.chatMissionAlert.text = "$mission"
            binding.tvMissionAdded.visibility = View.INVISIBLE
            if (mission == 3) {
                binding.tvMissionAdded.visibility = View.VISIBLE
            }
        } else {
            binding.chatMissionAlert.visibility = View.INVISIBLE
            binding.tvMissionAdded.visibility = View.INVISIBLE
            binding.chatMissionAlert.text = "$mission"
        }
        binding.pgbarChat.progress = mission
    }
    private fun init() {
        lifecycleScope.launch {
            val username = ApplicationClass.getInstance().getDataStore().nickname.first()
            messageList.add(ChatDTO("ELSE","${username}, 오늘 하루는 어땠어?"))
            postMessage(null)
        }

        binding.etChatTalk.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateButtonUI()
            }
            override fun afterTextChanged(s: Editable?) {
                updateButtonUI()
            }
        })
    }
}