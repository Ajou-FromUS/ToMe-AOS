package presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentMypageBinding

class MypageFragment: Fragment() {
    private lateinit var binding: FragmentMypageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        val fragmentManager = parentFragmentManager.beginTransaction()

        binding.btnMissionRecord.setOnClickListener {
            val fragment = MissionCheckFragment()
            fragmentManager.add(R.id.frame_mypage, fragment)
                .commitAllowingStateLoss()
        }
        binding.btnQna.setOnClickListener {
            val fragment = QnaFragment()
            fragmentManager.add(R.id.frame_mypage, fragment)
                .commitAllowingStateLoss()
        }
        binding.btnAccountSetting.setOnClickListener {
            val fragment = AccountSettingFragment()
            fragmentManager.add(R.id.frame_mypage, fragment)
                .commitAllowingStateLoss()
        }
        //개인
        binding.btnPrivatePolicy.setOnClickListener {

        }

        return binding.root
    }

}