package presentation.mission

import android.nfc.Tag
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentMissionDetailBinding
import presentation.MainActivity
import presentation.mission.decibel.MissionDecibelFragment
import presentation.mission.photo.MissionPhotoFragment
import presentation.mission.text.MissionTextFragment


class MissionDetailFragment : Fragment() {
    private lateinit var binding: FragmentMissionDetailBinding
    private lateinit var goButton: Button
    private lateinit var backButton: Button
    private lateinit var missionImage: ImageView
    private lateinit var typeText: TextView
    private lateinit var detailText: TextView

    companion object {
        private const val TEXT_TYPE = 0
        private const val PHOTO_TYPE = 1
        private const val DECIBEL_TYPE = 2
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMissionDetailBinding.inflate(inflater, container, false).apply {
            goButton = goMissionBtn
            backButton = backMissionBtn
            missionImage = detailImageView
            typeText = missionTypeText
            detailText = missionDetailText
        }
        var missionDetail = arguments?.getString("missionTitle")
        val missionType = arguments?.getInt("missionType")
        val missionID = arguments?.getInt("missionID")

        val missionTextFragment = MissionTextFragment()
        val missionDecibelFragment = MissionDecibelFragment()
        val missionPhotoFragment = MissionPhotoFragment()

        val bundle = Bundle(2)
        bundle.putString("missionTitle", missionDetail)
        bundle.putInt("missionID", missionID!!)

        missionDecibelFragment.arguments = bundle
        missionTextFragment.arguments = bundle
        missionPhotoFragment.arguments = bundle

        detailText.text = missionDetail

        when(missionType){
            PHOTO_TYPE -> {
                typeText.text = "찰칵 미션"
                missionImage.setImageResource(R.drawable.img_mission_camera)
            }
            DECIBEL_TYPE -> {
                typeText.text = "데시벨 미션"
                missionImage.setImageResource(R.drawable.img_mission_decibel)
            }
            TEXT_TYPE -> {
                typeText.text = "텍스트 미션"
                missionImage.setImageResource(R.drawable.img_mission_text)
            }
        }

        goButton.setOnClickListener {
            when(missionType){
                PHOTO_TYPE -> transactionChange(missionPhotoFragment)
                DECIBEL_TYPE -> transactionChange(missionDecibelFragment)
                TEXT_TYPE -> transactionChange(missionTextFragment)
            }
        }

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }
     private fun transactionChange(fragment: Fragment){
         val transaction = parentFragmentManager.beginTransaction()
         transaction.replace(R.id.main_frameLayout, fragment, "MISSION")
         transaction.addToBackStack(null);
         transaction.commit()
     }
}