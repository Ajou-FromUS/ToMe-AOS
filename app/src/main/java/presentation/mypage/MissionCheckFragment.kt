package presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import application.ApplicationClass
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentMissionCheckBinding
import data.dto.response.MissionRecordResponse
import data.service.ApiClient
import data.service.MyPageService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import presentation.mypage.adapter.MyPageAdapter
import java.time.LocalDate

class MissionCheckFragment: Fragment() {
    private lateinit var binding: FragmentMissionCheckBinding
    private lateinit var adapter: MyPageAdapter
    private lateinit var recyclerView: RecyclerView
    private var missionRecord: MissionRecordResponse? = null
    private var year = LocalDate.now().year
    private var month = LocalDate.now().monthValue
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMissionCheckBinding.inflate(inflater, container, false)

        binding.tvMissionDate.text = "${year}년 ${month}월"

        recyclerView = binding.recyclerMissionRecord
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = MyPageAdapter(requireContext(), missionRecord ?:MissionRecordResponse(emptyList()))
        recyclerView.adapter = adapter
        getMissions("2023-11")
        btnListener()
        return binding.root
    }
    private fun btnListener() {
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.missionPopup.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_datepicker, null)
            val yearPicker: NumberPicker = dialogView.findViewById(R.id.yearpicker_datepicker)
            val monthPicker: NumberPicker = dialogView.findViewById(R.id.monthpicker_datepicker)
            val btnCancel: Button = dialogView.findViewById(R.id.btn_date_cancel)
            val btnSave: Button = dialogView.findViewById(R.id.btn_date_save)

            yearPicker.wrapSelectorWheel = false
            monthPicker.wrapSelectorWheel = false
            yearPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            monthPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            yearPicker.minValue = 2023
            yearPicker.maxValue = 9999
            yearPicker.value = year
            monthPicker.minValue = 1
            monthPicker.maxValue = 12
            monthPicker.value = month
            val dialog = AlertDialog.Builder(requireContext()).apply {
                setView(dialogView)
            }.create()
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            btnSave.setOnClickListener {
                val now = LocalDate.now()
                if (yearPicker.value > now.year || (yearPicker.value >= now.year && monthPicker.value > now.monthValue)) {
                    Toast.makeText(requireContext(), "현재 날짜를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }else{
                    year = yearPicker.value
                    month = monthPicker.value
                    val yearMonth = "$year-$month"
                    binding.tvMissionDate.text = "${year}년 ${month}월"
                    getMissions(yearMonth)
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }
    private fun getMissions(yearMonth: String) {
        val client = ApiClient.getApiClient().create(MyPageService::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            val accessToken = ApplicationClass.getInstance().getDataStore().accessToken.first()
            val refreshToken = ApplicationClass.getInstance().getDataStore().refreshToken.first()
            try {
                val response = client.getMonthlyMission(accessToken, refreshToken, yearMonth)
                println(response)
                adapter.updateData(response)
            } catch (e: Exception) {
                e.printStackTrace()
                adapter.updateData(MissionRecordResponse(emptyList()))
                Toast.makeText(requireContext(),"다시 시도해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}