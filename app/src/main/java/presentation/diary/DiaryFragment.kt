package presentation.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentDiaryBinding

class DiaryFragment : Fragment() {
    private lateinit var binding: FragmentDiaryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryBinding.inflate(inflater, container, false)
        binding.btnWriteDiary.setOnClickListener {
            // DiaryWriteFragment로 이동하는 코드를 작성
            val fragment = DiaryWriteFragment()

            // Fragment 전환을 위한 트랜잭션 시작
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.diary_framelayout, fragment)
            transaction.commit()
        }
        // Inflate the layout for this fragment

        return binding.root
    }

}