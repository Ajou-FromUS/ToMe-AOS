package presentation.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
        val hasMissionToday = arguments?.getBoolean("hasMission")

        Log.d("home has mission", hasMissionToday.toString())

        binding.toTalkBtn.setOnClickListener {
            var intent = Intent(context, ChatActivity::class.java)
            startActivity(intent)
        }

        binding.checkMissionBtn.setOnClickListener{
            if(hasMissionToday == true) {
                val fragment = MissionFragment()
                // Fragment 전환을 위한 트랜잭션 시작
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(com.example.tome_aos.R.id.main_frameLayout, fragment)
                transaction.addToBackStack(null);
                transaction.commit()

                mainActivity.changeMainTitle(1, null)
            }else if(hasMissionToday == false){
                Toast.makeText(context, "현재 할 수 있는 미션이 없어요!", Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root
    }
}

