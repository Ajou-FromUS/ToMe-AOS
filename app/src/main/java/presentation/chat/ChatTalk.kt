package presentation.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tome_aos.databinding.FragmentChatTalkBinding

class ChatTalk : Fragment() {
    private lateinit var binding: FragmentChatTalkBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatTalkBinding.inflate(inflater, container, false)
        return binding.root
    }
}