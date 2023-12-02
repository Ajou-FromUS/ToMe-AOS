package presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import application.ApplicationClass
import com.example.tome_aos.databinding.FragmentAccountSettingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import presentation.login.LoginActivity

class AccountSettingFragment: Fragment() {
    private lateinit var binding: FragmentAccountSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountSettingBinding.inflate(inflater, container, false)
        btnListener()
        return binding.root
    }
    private fun btnListener() {
        val fragmentManager = parentFragmentManager
        //뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            fragmentManager.popBackStack()
        }
        //닉네임 수정
        binding.btnNicknameModify.setOnClickListener {

        }
        //로그아웃
        binding.btnLogout.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                //Token remove
                ApplicationClass.getInstance().getDataStore().removeTokens()
                requireActivity().finish()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }
        //회원탈퇴
        binding.btnResign.setOnClickListener {

        }
    }
}