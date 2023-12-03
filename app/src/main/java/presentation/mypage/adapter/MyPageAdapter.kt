package presentation.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tome_aos.R
import data.dto.response.MissionRecordResponse
import java.text.SimpleDateFormat
import java.util.*

class MyPageAdapter(private val context: Context, private var missionRecordResponse: MissionRecordResponse) :
    RecyclerView.Adapter<MyPageAdapter.MissionViewHolder>() {
    inner class MissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mission: TextView = itemView.findViewById(R.id.tv_mission)
        val border: LinearLayout = itemView.findViewById(R.id.mission_date_border)
        val date: TextView = itemView.findViewById(R.id.tv_mission_date)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mission_date, parent, false)
        return MissionViewHolder(view)
    }
    override fun onBindViewHolder(holder: MissionViewHolder, position: Int) {
        val missionList = missionRecordResponse.data

        if (position == 0 || isDifferentDate(missionList[position].mDate, missionList[position - 1].mDate)) {
            holder.border.visibility = View.VISIBLE
            val missionDate = missionList[position].mDate.substring(8,10) + "일"
            holder.date.text = missionDate
            val name = "M" + missionList[position].mid + "."
            holder.mission.text = name
        } else {
            val name = "M" + missionList[position].mid + "."
            holder.mission.text = name
        }
    }
    override fun getItemCount(): Int {
        return missionRecordResponse.data.size
    }
    private fun isDifferentDate(date1: String, date2: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        val parsedDate1 = sdf.parse(date1)
        val parsedDate2 = sdf.parse(date2)

        val calendar1 = Calendar.getInstance()
        calendar1.time = parsedDate1!!

        val calendar2 = Calendar.getInstance()
        calendar2.time = parsedDate2!!

        return calendar1.get(Calendar.DAY_OF_MONTH) != calendar2.get(Calendar.DAY_OF_MONTH) ||
                calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH) ||
                calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR)
    }
    fun updateData(newMissionRecordResponse: MissionRecordResponse) {
        missionRecordResponse = newMissionRecordResponse
        notifyDataSetChanged()
    }
}