package presentation.mission

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentMissionBinding
import com.example.tome_aos.databinding.FragmentMissionDetailBinding
import kotlinx.coroutines.flow.combine
import presentation.MainActivity


class MissionDetailFragment : Fragment() {
    private lateinit var binding: FragmentMissionDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMissionDetailBinding.inflate(inflater, container, false)

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation(true)

        binding.backMissionBtn.setOnClickListener {

            parentFragmentManager.beginTransaction().apply{
                replace(com.example.tome_aos.R.id.mission_box_frame, MissionDetailFragment())
                addToBackStack(null)
                commit()
            }
        }
        return inflater.inflate(R.layout.fragment_mission_detail, container, false)
    }

}