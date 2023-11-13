package presentation.mission

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentMissionCompleteBinding
import com.example.tome_aos.databinding.FragmentMissionTextBinding
import presentation.home.HomeFragment

class MissionCompleteFragment : Fragment() {
    private lateinit var binding: FragmentMissionCompleteBinding
    private lateinit var otherMissionBtn: Button
    private lateinit var homeBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMissionCompleteBinding.inflate(inflater, container, false).apply {
            otherMissionBtn = goOtherMissionBtn
            homeBtn = backHomeBtn
        }
        val homeFragment = HomeFragment()
        val missionFragment = MissionFragment()

        otherMissionBtn.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, missionFragment)
                .addToBackStack(null)
                .commit()
        }

        homeBtn.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, homeFragment)
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

}