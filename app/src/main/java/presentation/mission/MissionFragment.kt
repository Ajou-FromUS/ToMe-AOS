package presentation.mission

import android.content.Context
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
import presentation.MainActivity
import presentation.home.HomeFragment
import presentation.mission.adapter.MissionAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate


class MissionFragment : Fragment() {
    private lateinit var binding: FragmentMissionBinding
    private lateinit var recyclerView: RecyclerView
    private val missionDetailFragment = MissionDetailFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMissionBinding.inflate(inflater, container, false)

        val mainActivity = activity as MainActivity

        getMission()
        val homeFragment = HomeFragment()

        val bundle = Bundle(1) // 파라미터는 전달할 데이터 개수

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

//        binding.missionBox1.setOnClickListener {
//            bundle.putString("missionType", "photo") // key , value
//            missionDetailFragment.arguments = bundle
//            val transaction = parentFragmentManager.beginTransaction()
//            transaction.replace(com.example.tome_aos.R.id.main_frameLayout, missionDetailFragment)
//            transaction.addToBackStack(null);
//            transaction.commit()
//        }

        return binding.root
    }


    private fun setMissionList(response: List<MissionResponse.Data>?){
        if (response != null) {
            recyclerView = binding.missionListLayout
            val missionAdapter = MissionAdapter(response)
            recyclerView.adapter = missionAdapter

            missionAdapter.setOnItemclickListner(object: MissionAdapter.OnItemClickListner{
                    override fun onItemClick(position: Int) {
                        val bundle = Bundle(2)
                        //bundle.putSerializable("missionDetail", response[position]) // key , value
                        response[position].mission?.type?.let { bundle.putInt("missionType", it) }
                        bundle.putString("missionTitle", response[position].mission?.title)
                        missionDetailFragment.arguments = bundle
                        val transaction = parentFragmentManager.beginTransaction()
                        transaction.replace(R.id.main_frameLayout, missionDetailFragment)
                        transaction.addToBackStack(null);
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