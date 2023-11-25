package presentation.mission.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tome_aos.R
import data.dto.response.MissionResponse
import presentation.mission.MissionDetailFragment


class MissionAdapter(private val missionList: List<MissionResponse.Data>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val PHOTO_TYPE = 0
        private const val DECIBEL_TYPE = 1
        private const val TEXT_TYPE = 2
    }

    inner class MissionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.mission_image)
        val title : TextView = itemView.findViewById(R.id.mission_title)
        val content : TextView = itemView.findViewById(R.id.mission_body)
        val existLayout: LinearLayout = itemView.findViewById(R.id.exist_mission)
        val notExistLayout: LinearLayout = itemView.findViewById(R.id.not_exist_mission)
        val missionBox: LinearLayout = itemView.findViewById(R.id.mission_box)
    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mission_box, parent, false)

        return MissionHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val missionData = missionList[position]
        val missionDetail = missionData.mission
        val missionHolder = holder as MissionHolder

        if (missionData.is_completed == true) {
            missionHolder.missionBox.setBackgroundColor(2)

        }else if(missionData.is_completed == false){
            when (missionDetail?.type) {
                PHOTO_TYPE -> {
                    missionHolder.title.text = "찰칵 미션"
                    missionHolder.image.setImageResource(R.drawable.img_mission_camera)
                    missionHolder.content.text = missionDetail.title
                }
                DECIBEL_TYPE -> {
                    missionHolder.title.text = "데시벨 미션"
                    missionHolder.image.setImageResource(R.drawable.img_mission_decibel)
                    missionHolder.content.text = missionDetail.title
                }
                TEXT_TYPE -> {
                    missionHolder.title.text = "텍스트 미션"
                    missionHolder.image.setImageResource(R.drawable.img_mission_text)
                    missionHolder.content.text = missionDetail.title
                }
            }
        }else{
            missionHolder.image.visibility = View.GONE
            missionHolder.existLayout.visibility = View.GONE
            missionHolder.notExistLayout.visibility = View.VISIBLE
        }

        missionHolder.missionBox.setOnClickListener {
            itemClickListner.onItemClick(position)
        }
    }

    interface OnItemClickListner{
        fun onItemClick(position: Int)
    }
    //객체 저장 변수
    private lateinit var itemClickListner: OnItemClickListner

    //객체 전달 메서드
    fun setOnItemclickListner(onItemClickListner: OnItemClickListner){
        itemClickListner = onItemClickListner
    }

    override fun getItemCount(): Int {
        return missionList.size
    }

}