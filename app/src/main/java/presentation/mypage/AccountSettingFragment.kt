package presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tome_aos.databinding.FragmentAccountSettingBinding

class AccountSettingFragment: Fragment() {
    private lateinit var binding: FragmentAccountSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountSettingBinding.inflate(inflater, container, false)

        val fragmentManager = parentFragmentManager.beginTransaction()
        //뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            fragmentManager.remove(this).commitAllowingStateLoss()
        }
        //닉네임 수정
        binding.btnNicknameModify.setOnClickListener {

        }
        //로그아웃
        binding.btnLogout.setOnClickListener {

        }
        //회원탈퇴
        binding.btnResign.setOnClickListener {

        }
        return binding.root
    }
}