package presentation.home

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tome_aos.databinding.FragmentHomeBinding
import presentation.mission.MissionFragment


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.checkMissionBtn.setOnClickListener{

            val fragment = MissionFragment()
            // Fragment 전환을 위한 트랜잭션 시작
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(com.example.tome_aos.R.id.main_frameLayout, fragment)
            transaction.addToBackStack(null);
            transaction.commit()
        }


        return binding.root
    }



}

