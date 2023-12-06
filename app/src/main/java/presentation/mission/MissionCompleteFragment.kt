package presentation.mission

import android.content.Intent
import android.media.MediaPlayer
import android.media.SoundPool
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
import presentation.MainActivity
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

        val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.mp_mission_complete)
        mediaPlayer.setLooping(false)
        mediaPlayer.start()

        val missionFragment = MissionFragment()

        otherMissionBtn.setOnClickListener{
            //soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, missionFragment)
                .addToBackStack(null)
                .commit()
        }

        homeBtn.setOnClickListener{
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}