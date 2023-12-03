package presentation.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tome_aos.R
import data.dto.response.MissionRecordResponse

class MyPageAdapter(private val context: Context, private var missionRecordResponse: MissionRecordResponse) :
    RecyclerView.Adapter<MyPageAdapter.MissionViewHolder>() {

    inner class MissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val missionFirst: TextView = itemView.findViewById(R.id.tv_mission_first)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mission_date, parent, false)
        return MissionViewHolder(view)
    }
    override fun onBindViewHolder(holder: MissionViewHolder, position: Int) {
        val mission = missionRecordResponse.data
//        holder.missionDate.text = mission.mDate.toString()
        if (mission.isNotEmpty()) {
            val name = "M"+"${mission[position].mid}"+"."
            holder.missionFirst.text = name
        } else {
            holder.missionFirst.visibility = View.GONE
        }
//        holder.missionSecond.text = if (mission.isCompleted) "Completed" else "Incomplete"
//        holder.missionThird.text = if (mission.isCompleted) "Completed" else "Incomplete"
    }
    override fun getItemCount(): Int {
        return missionRecordResponse.data.size
    }
    fun updateData(newMissionRecordResponse: MissionRecordResponse) {
        missionRecordResponse = newMissionRecordResponse
        notifyDataSetChanged()
    }
}