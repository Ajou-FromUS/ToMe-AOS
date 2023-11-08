package presentation.mission

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tome_aos.databinding.FragmentMissionBinding
import presentation.MainActivity


class MissionFragment : Fragment() {
    private lateinit var binding: FragmentMissionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMissionBinding.inflate(inflater, container, false)

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation(false)

        binding.missionBox1.setOnClickListener {
            val fragment = MissionDetailFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(com.example.tome_aos.R.id.mission_box_frame, fragment)
            binding.missionBoxLayout.visibility = View.GONE
            transaction.commit()
        }


        return binding.root
    }

}