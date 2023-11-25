package presentation.mission

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentMissionBinding
import com.example.tome_aos.databinding.FragmentMissionDetailBinding
import kotlinx.coroutines.flow.combine
import presentation.MainActivity
import presentation.mission.camera.GalleryActivity
import presentation.mission.decibel.MissionDecibelFragment
import presentation.mission.text.MissionTextFragment


class MissionDetailFragment : Fragment() {
    private lateinit var binding: FragmentMissionDetailBinding
    private lateinit var goButton: Button
    private lateinit var backButton: Button
    val uri = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMissionDetailBinding.inflate(inflater, container, false).apply {
            goButton = goMissionBtn
            backButton = backMissionBtn
        }
        var missionDetail = arguments?.getString("missionTitle")
        val missionType = arguments?.getInt("missionType")

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation(true)

        val missionFragment = MissionFragment()
        val missionTextFragment = MissionTextFragment()
        val missionDecibelFragment = MissionDecibelFragment()
        val transaction = parentFragmentManager.beginTransaction()

        goButton.setOnClickListener {
            when(missionType){
                0 -> openGallery()
                1 -> {
                    transaction.replace(R.id.main_frameLayout, missionDecibelFragment)
                    transaction.addToBackStack(null);
                    transaction.commit() }
                2 -> {
                    transaction.replace(R.id.main_frameLayout, missionTextFragment)
                    transaction.addToBackStack(null)
                    transaction.commit() }
            }
        }

        backButton.setOnClickListener {
            transaction.replace(R.id.main_frameLayout, missionFragment)
            transaction.addToBackStack(null);
            transaction.commit()
        }
        return binding.root
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activityResult.launch(intent)
    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == AppCompatActivity.RESULT_OK && it.data != null){
            val uri = it.data!!.data

            Glide.with(this)
                .load(uri)
                .into(binding.detailImageView)

            goButton.text = "티오에게 보여주기"
        }
    }

}