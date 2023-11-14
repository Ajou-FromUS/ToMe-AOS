package presentation.mission.decibel

import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.tome_aos.databinding.FragmentMissionDecibelBinding

class MissionDecibelFragment : Fragment() {
    private lateinit var binding: FragmentMissionDecibelBinding
    private lateinit var showBtn: Button
    private lateinit var againBtn: Button
    private lateinit var dbText: TextView
    //private var audioRecord: AudioRecord? = null
    private var isRecording = true
    private var minSize = 0
    private val db = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMissionDecibelBinding.inflate(inflater, container, false).apply {
            showBtn = showToDecidelBtn
            againBtn = backDecidelBtn
            dbText = decibelValue
        }
        //startCheck()
//        if (isRecording) {
//            dbText.text = level.toString()
//        }
        if(!isRecording){
            showBtn.visibility = View.VISIBLE
            againBtn.visibility = View.VISIBLE
        }
        return binding.root
    }

    private fun changeDbText(){
        val dbValue = "${db} db"
        val sps = SpannableStringBuilder(dbValue)
        val start = 0
        val end = db.toString().length
        sps.setSpan(RelativeSizeSpan(2.0f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        sps.setSpan(ForegroundColorSpan(Color.BLUE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 텍스트뷰에 저장합니다.
        dbText.text = sps
    }

//    val level: Int
//        get() {
//            val buffer = ShortArray(minSize)
//            audioRecord?.read(buffer, 0, minSize)
//            return buffer.max().toInt()
//        }

//    fun startCheck() {
//        minSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
//        if (context?.let { ActivityCompat.checkSelfPermission(it, RECORD_AUDIO) } != PERMISSION_GRANTED) {
//            Log.d("record", "==> record audio permission not granted")
//            return
//        }
//        audioRecord = AudioRecord(
//            MediaRecorder.AudioSource.MIC,
//            8000,
//            AudioFormat.CHANNEL_IN_MONO,
//            AudioFormat.ENCODING_PCM_16BIT,
//            minSize
//        )
//        audioRecord?.startRecording()
//    }

//    private fun stopCheck() {
//        audioRecord?.stop()
//    }

}