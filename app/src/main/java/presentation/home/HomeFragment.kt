package presentation.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import application.ApplicationClass
import com.example.tome_aos.databinding.FragmentHomeBinding
import data.dto.response.InitResponse
import data.dto.response.MissionResponse
import data.service.ApiClient
import data.service.InitClient
import data.service.MissionService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import presentation.MainActivity
import presentation.chat.ChatActivity
import presentation.mission.MissionFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var has_mission_today = false
    private var nickname = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val mainActivity = activity as MainActivity

        getInit()

        mainActivity.changeMainTitle(0, nickname)

        binding.toTalkBtn.setOnClickListener {
            var intent = Intent(context, ChatActivity::class.java)
            startActivity(intent)
        }

        binding.checkMissionBtn.setOnClickListener{
            if(has_mission_today == true) {
                val fragment = MissionFragment()
                // Fragment 전환을 위한 트랜잭션 시작
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(com.example.tome_aos.R.id.main_frameLayout, fragment)
                transaction.addToBackStack(null);
                transaction.commit()

                mainActivity.changeMainTitle(1, null)
            }
        }


        return binding.root
    }



    private fun getInit() {
        val client = ApiClient.getApiClient().create(InitClient::class.java)
        lifecycleScope.launch {
            val accessToken = ApplicationClass.getInstance().getDataStore().accessToken.first()
            val refreshToken = ApplicationClass.getInstance().getDataStore().refreshToken.first()
            client.getInit(accessToken, refreshToken).enqueue(object : Callback<InitResponse> {
                override fun onResponse(call: Call<InitResponse>, response: Response<InitResponse>) {
                    if (response.isSuccessful) {
                        Log.d("initResponse", response.body().toString())
                        val initData: InitResponse.Data = response.body()!!.initData
                        nickname = initData.nickname
                        has_mission_today = initData.has_mission_today
                    } else {
                        Log.d("initResponse", "HTTP 오류")
                    }
                }
                override fun onFailure(call: Call<InitResponse>, t: Throwable) {
                    Log.d("initFail", t.toString())
                    t.printStackTrace()
                }
            })
        }
    }
}

