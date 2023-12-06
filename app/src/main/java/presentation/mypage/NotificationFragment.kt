package presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tome_aos.databinding.FragmentNotificationBinding

class NotificationFragment: Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        btnListener()
        return binding.root
    }
    private fun btnListener() {
        val fragmentManager = parentFragmentManager
        binding.btnBack.setOnClickListener {
            fragmentManager.popBackStack()
        }
        //binding.btnSwitch
        //스위치 변경 시 알림 설정
    }
}