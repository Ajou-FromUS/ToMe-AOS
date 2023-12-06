package presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentMypageBinding
import presentation.MainActivity

class MypageFragment: Fragment() {
    private lateinit var binding: FragmentMypageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        btnListener()
        return binding.root
    }
    private fun btnListener() {
        beginTransaction(binding.btnNotify, NotificationFragment(), "NOTIFICATION")
        beginTransaction(binding.btnMissionRecord, MissionCheckFragment(), "MISSION_CHECK")
        beginTransaction(binding.btnQna, QnaFragment(), "QNA")
        beginTransaction(binding.btnAccountSetting, AccountSettingFragment(), "ACCOUNT_SETTING")
//        beginTransaction(binding.btnPrivatePolicy,())
    }
    private fun beginTransaction(button: Button, fragment: Fragment, tag: String){
        button.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .add(R.id.frame_mypage, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }
}