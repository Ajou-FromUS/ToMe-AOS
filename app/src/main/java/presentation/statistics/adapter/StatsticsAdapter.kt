package presentation.statistics.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tome_aos.R



class StatsticsAdapter(private val context: Context, private var missionCountList: List<Int>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val countbox: View = itemView.findViewById(R.id.count_box)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_stat_contribution, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mcount = missionCountList[position]
        val missionHolder = holder as ViewHolder
        when (mcount) {
            0 -> missionHolder.countbox.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_disabled2))
            1 -> missionHolder.countbox.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_sub2))
            2 -> missionHolder.countbox.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_sub1))
            3 -> missionHolder.countbox.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_main))
            else -> missionHolder.countbox.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_transparent))
        }
    }
    override fun getItemCount(): Int {
        return missionCountList.size
    }
    fun updateData(newCountList: List<Int>) {
        missionCountList = newCountList
        notifyDataSetChanged()
    }
}