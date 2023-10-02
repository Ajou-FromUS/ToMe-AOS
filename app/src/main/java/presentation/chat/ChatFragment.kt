package presentation.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentChatBinding

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        var instVisibility = binding.tvChatInst.isVisible
        val ibtnChatQuestion = binding.ibtnChatQuestion
        val btnStartChat = binding.btnStartChat
        ibtnChatQuestion.setOnClickListener {
            if(!instVisibility) {
                instVisibility = true
            }else{
                instVisibility = false
            }
        }
        btnStartChat.setOnClickListener {
            val fragment = ChatTalk()
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, fragment, "chatTalk")
                .commit()
            //어플리케이션 종료 전까지는 계속 1단계 화면에 남아있어야 하기에 replace
        }
        binding.root.setOnClickListener {
            instVisibility = false
        }
        return binding.root
    }
}