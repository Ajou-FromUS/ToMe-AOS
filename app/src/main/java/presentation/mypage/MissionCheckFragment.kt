package presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tome_aos.databinding.FragmentMissionCheckBinding

class MissionCheckFragment: Fragment() {
    private lateinit var binding: FragmentMissionCheckBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMissionCheckBinding.inflate(inflater, container, false)

        return binding.root
    }
}