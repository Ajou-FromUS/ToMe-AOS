package presentation.mission

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import application.ApplicationClass
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentMissionBinding
import data.dto.response.MissionResponse
import data.service.ApiClient
import data.service.MissionService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import presentation.mission.adapter.MissionAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate


class MissionFragment : Fragment() {
    private lateinit var binding: FragmentMissionBinding
    private lateinit var recyclerView: RecyclerView
    private val missionDetailFragment = MissionDetailFragment()

    //val completeImage = mutableListOf<ImageView>(includeBinding.checkMission1, includeBinding.checkMission2, includeBinding.checkMission3)
    private var completeCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMissionBinding.inflate(inflater, container, false)

        getMission()

        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                parentFragmentManager.popBackStack()
            }
            false
        }

        return binding.root
    }

    private fun completeImageView(completeList: BooleanArray){
        if(completeList[0])
            binding.complete3.setImageResource(R.drawable.ic_mission_complete)
        if(completeList[1])
            binding.complete2.setImageResource(R.drawable.ic_mission_complete)
        if(completeList[2])
            binding.complete1.setImageResource(R.drawable.ic_mission_complete)
    }


    private fun setMissionList(response: List<MissionResponse.Data>?){
        if (response != null) {
            val completelist: BooleanArray = booleanArrayOf(response[0].is_completed?: false, response[1].is_completed?:false, response[2].is_completed?:false)
            completeImageView(completelist)
            recyclerView = binding.missionListLayout
            val missionAdapter = MissionAdapter(response)
            recyclerView.adapter = missionAdapter

            missionAdapter.setOnItemclickListner(object: MissionAdapter.OnItemClickListner{
                    override fun onItemClick(position: Int) {
                        val bundle = Bundle(3)
                        bundle.putInt("missionType", response[position].mission?.type!!)
                        bundle.putString("missionTitle", response[position].mission?.title)
                        bundle.putInt("missionID", response[position].id!!)
                        bundle.putBooleanArray("completeList", completelist)
                        missionDetailFragment.arguments = bundle
                        val transaction = parentFragmentManager.beginTransaction()
                        transaction.replace(R.id.main_frameLayout, missionDetailFragment, "MISSION")
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                })

            val layoutManager = LinearLayoutManager(context)
            recyclerView.layoutManager = layoutManager
        }
    }

    private fun getMission() {
        val client = ApiClient.getApiClient().create(MissionService::class.java)
        lifecycleScope.launch {
            val accessToken = ApplicationClass.getInstance().getDataStore().accessToken.first()
            val refreshToken = ApplicationClass.getInstance().getDataStore().refreshToken.first()
            val today = LocalDate.now().toString()
            client.getMissions(accessToken, refreshToken, today).enqueue(object : Callback<MissionResponse> {
                override fun onResponse(call: Call<MissionResponse>, response: Response<MissionResponse>) {
                    if (response.isSuccessful) {
                        val missionsData: List<MissionResponse.Data>? = response.body()?.missionsData
                        setMissionList(missionsData)
                    } else {
                        println("HTTP 오류: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<MissionResponse>, t: Throwable) {
                    Log.d("fail", t.toString())
                    t.printStackTrace()
                }
            })
        }
    }

}