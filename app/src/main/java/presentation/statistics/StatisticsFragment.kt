package presentation.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import application.ApplicationClass
import com.bumptech.glide.Glide
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import data.service.ApiClient
import data.service.StatsService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import presentation.statistics.adapter.StatsticsAdapter
import java.time.LocalDate
import kotlin.collections.ArrayList

class StatisticsFragment: Fragment() {
    private lateinit var binding: FragmentStatisticsBinding
    private lateinit var adapter: StatsticsAdapter
    private var year = LocalDate.now().year
    private var month = LocalDate.now().monthValue
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false)


        val recyclerView: RecyclerView = binding.recyclerContribution
        val layoutManager = GridLayoutManager(requireContext(), 10)
        recyclerView.layoutManager = layoutManager
        adapter = StatsticsAdapter(requireContext(), emptyList()) // 빈 상태의 어댑터 설정
        recyclerView.adapter = adapter

        val yearMonth = "$year-$month"
        binding.tvStatDate.text = "${year}년 ${month}월"
        binding.tvAnalyze.text = "티오가 ${month}월을 분석했어요."
        btnListener()
        getStats(yearMonth)

        return binding.root
    }

    private fun getStats(yearMonth: String) {
        val client = ApiClient.getApiClient().create(StatsService::class.java)
        lifecycleScope.launch {
            val accessToken = ApplicationClass.getInstance().getDataStore().accessToken.first()
            val refreshToken = ApplicationClass.getInstance().getDataStore().refreshToken.first()
            println(yearMonth)
            try {
                val response = client.getStatisticsForYearMonth(accessToken, refreshToken, yearMonth)
                //println(accessToken +"\n"+ refreshToken)
                //키워드 이미지 설정
//                println(response)
                Glide.with(this@StatisticsFragment)
                    .load(response.data.keywordImg)
                    .error(R.drawable.img_error_wordcloud)
                    .into(binding.ivMonthlyKeyword)
                //감정 분석 차트 설정
                val pieChart: PieChart = binding.pieChart
                val (neutral, positive, negative) = response.data.emotionPercentages
                setChart(pieChart, neutral, negative, positive)
                //잔디 설정
                adapter.updateData(response.data.completedMissionCounts)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(),"다시 시도해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setChart(pieChart: PieChart, neu: Float, neg: Float, pos: Float) {
        val pieEntries = ArrayList<PieEntry>()
        pieEntries.add(PieEntry(neu, "\uD83D\uDE10"))
        pieEntries.add(PieEntry(neg, "\uD83D\uDE22"))
        pieEntries.add(PieEntry(pos, "\uD83D\uDE00"))
        val dataSet = PieDataSet(pieEntries, "")
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.color_font4)
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${String.format("%.1f", value)}%"
            }
        }
        dataSet.colors = listOf(
            ContextCompat.getColor(requireContext(), R.color.color_main),
            ContextCompat.getColor(requireContext(), R.color.color_sub1),
            ContextCompat.getColor(requireContext(), R.color.color_sub2))

        val data = PieData(dataSet)

        pieChart.data = data
        pieChart.setTouchEnabled(false)
        pieChart.description.isEnabled = false
        pieChart.isRotationEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.setDrawEntryLabels(true)
        pieChart.setUsePercentValues(true)
        pieChart.isDrawHoleEnabled = false
        pieChart.animateY(600)

        val emotionStringId = when (maxOf(neu, neg, pos)) {
            pos -> R.string.positive_emotion
            neg -> R.string.negative_emotion
            neu -> R.string.neutral_emotion
            else -> R.string.default_emotion
        }
        binding.tvEmotion.setText(emotionStringId)
    }
    private fun btnListener() {
        binding.statsPopup.setOnClickListener {
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
                    binding.tvStatDate.text = "${year}년 ${month}월"
                    binding.tvAnalyze.text = "티오가 ${month}월을 분석했어요."
                    getStats(yearMonth)
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }
}