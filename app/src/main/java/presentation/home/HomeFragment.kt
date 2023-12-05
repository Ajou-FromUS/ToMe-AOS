package presentation.home

import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentHomeBinding
import presentation.MainActivity
import presentation.chat.ChatActivity
import presentation.mission.MissionFragment


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val mainActivity = activity as MainActivity
        val nickName = arguments?.getString("nickname")
        val hasMissionToday = arguments?.getBoolean("hasMission")

        binding.mainTitleText.text = "${nickName},\n오늘도 나랑 대화하자."

        val soundPool = SoundPool.Builder().build()
        var soundId = soundPool.load(requireContext(), R.raw.mp_bubble, 1)

        Log.d("home has mission", hasMissionToday.toString())

        binding.toTalkBtn.setOnClickListener {
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
            var intent = Intent(context, ChatActivity::class.java)
            startActivity(intent)
        }

        binding.checkMissionBtn.setOnClickListener{
            if(hasMissionToday == true) {
                soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)

                val fragment = MissionFragment()
                // Fragment 전환을 위한 트랜잭션 시작
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(com.example.tome_aos.R.id.main_frameLayout, fragment)
                transaction.addToBackStack(null);
                transaction.commit()

            }else if(hasMissionToday == false){
                Toast.makeText(context, "현재 할 수 있는 미션이 없어요!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}

