package presentation.mission

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tome_aos.databinding.FragmentMissionBinding
import presentation.MainActivity
import presentation.home.HomeFragment


class MissionFragment : Fragment() {
    private lateinit var binding: FragmentMissionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMissionBinding.inflate(inflater, container, false)
        binding.missionBoxLayout.visibility = View.VISIBLE
        val homeFragment = HomeFragment()
        val missionDetailFragment = MissionDetailFragment()
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation(false)

        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(com.example.tome_aos.R.id.main_frameLayout, homeFragment)
                transaction.addToBackStack(null);
                transaction.commit()
            }
            false
        }

        binding.missionBox1.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(com.example.tome_aos.R.id.main_frameLayout, missionDetailFragment)
            transaction.addToBackStack(null);
            transaction.commit()
        }

        return binding.root
    }

}