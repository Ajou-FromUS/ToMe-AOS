package presentation.chat

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentChatTalkBinding
import presentation.chat.Adapter.chatAdapter
import androidx.recyclerview.widget.RecyclerView


class ChatTalk : Fragment() {
    private lateinit var binding: FragmentChatTalkBinding
    private lateinit var et: EditText
    private lateinit var ibtn: ImageButton
    private val messageList = mutableListOf<String>()
    private lateinit var adapter: chatAdapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatTalkBinding.inflate(inflater, container, false)
        et = binding.etChatTalk
        ibtn = binding.ibtnChatSend
        recyclerView = binding.chatRecycler
        adapter = chatAdapter(messageList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateButtonUI()
            }
            override fun afterTextChanged(s: Editable?) {
                updateButtonUI()
            }
        })

        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }

        ibtn.setOnClickListener {
            val message = et.text.toString()
            if(message.isNotEmpty()) {
                messageList.add(message)
                adapter.notifyItemInserted(messageList.size - 1)
                et.text.clear()
                recyclerView.scrollToPosition(messageList.size - 1)
            }

        }

        return binding.root
    }
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
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        imm.hideSoftInputFromWindow(et.windowToken, 0)
        et.clearFocus()
    }
}