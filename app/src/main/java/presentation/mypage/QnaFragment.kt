package presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tome_aos.databinding.FragmentQnaBinding

class QnaFragment: Fragment() {
    private lateinit var binding: FragmentQnaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQnaBinding.inflate(inflater, container, false)
        btnListener()
        return binding.root
    }
    private fun btnListener() {
        val fragmentManager = parentFragmentManager
        //뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            fragmentManager.popBackStack()
        }
        //카카오톡 플러스 친구 링크 이동
        binding.btnKakaoPlus.setOnClickListener {

        }
    }
}