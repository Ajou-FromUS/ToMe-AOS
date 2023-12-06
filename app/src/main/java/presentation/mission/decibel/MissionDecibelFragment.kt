package presentation.mission.decibel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaRecorder
import android.os.Build
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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import application.ApplicationClass
import com.bumptech.glide.Glide
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentMissionDecibelBinding
import com.google.gson.GsonBuilder
import data.dto.request.MissionCompleteRequest
import data.dto.response.MissionResponse
import data.service.ApiClient
import data.service.MissionCompleteService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import presentation.MainActivity
import presentation.mission.MissionCompleteFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MissionDecibelFragment : Fragment() {
    private lateinit var binding: FragmentMissionDecibelBinding
    private lateinit var showBtn: Button
    private lateinit var againBtn: Button
    private lateinit var dbText: TextView
    private lateinit var missionTitleText: TextView
    private lateinit var explainText: TextView

    private var recorder: MediaRecorder? = null
    private var isRecording = false
    private var job: Job? = null
    private var filePath = ""

    private var db: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMissionDecibelBinding.inflate(inflater, container, false).apply {
            showBtn = showToDecidelBtn
            againBtn = backDecidelBtn
            dbText = decibelValue
            missionTitleText = decibelTitleText
            explainText = decibelExplainText
        }

        val missionTitle = arguments?.getString("missionTitle")
        val missionID = arguments?.getInt("missionID")

        missionTitleText.text = missionTitle

        // 권한 부여 여부
        val isEmpower = ContextCompat.checkSelfPermission(context as MainActivity,
            Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context as MainActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

        Log.d("Record isEmpower", isEmpower.toString())
        // 권한 부여 되지 않았을경우
        if (isEmpower) {
            empowerRecordAudioAndWriteReadStorage()
        // 권한 부여 되었을 경우
        } else {
            startRecord()
        }

        changeDbText(0)

        showBtn.setOnClickListener {
            patchDecibelMission(missionID)
        }
        againBtn.setOnClickListener {
            startRecord()
        }

        return binding.root
    }

    private fun patchDecibelMission(missionID: Int?){
        val client = ApiClient.getApiClient().create(MissionCompleteService::class.java)
        lifecycleScope.launch {
            val accessToken = ApplicationClass.getInstance().getDataStore().accessToken.first()
            val refreshToken = ApplicationClass.getInstance().getDataStore().refreshToken.first()
            val missionCompleteRequest = null
            val requestBody = GsonBuilder()
                .serializeNulls().create()
                .toJson(missionCompleteRequest)
                .toRequestBody("application/json".toMediaTypeOrNull())
            client.patchMissions(accessToken, refreshToken, missionID, requestBody).enqueue(object :
                Callback<MissionResponse.Data> {
                override fun onResponse(call: Call<MissionResponse.Data>, response: Response<MissionResponse.Data>) {
                    if (response.isSuccessful) {
                        completePage()
                    } else {
                        println("HTTP 오류: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<MissionResponse.Data>, t: Throwable) {
                    Log.d("fail", t.toString())
                    t.printStackTrace()
                }
            })
        }
    }

    private fun completePage(){
        val missionCompleteFragment = MissionCompleteFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frameLayout, missionCompleteFragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }

    private fun completeDecibelCheck(){
        dbText.text = "50"
        showBtn.visibility = View.VISIBLE
        againBtn.visibility = View.VISIBLE
    }

    // 레코딩, 파일 읽기 쓰기 권한부여
    private fun empowerRecordAudioAndWriteReadStorage(){
        val permissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(context as MainActivity, permissions,0)
    }

    private fun startRecord() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d("Record SDK", "In")
            recorder = MediaRecorder(context as MainActivity)
        }
        Log.d("Record", recorder.toString())
        //외부 저장소 내 개별앱 공간에 저장
        val basePath = requireContext().getExternalFilesDir(null)?.absolutePath

        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC) //외부에서 들어오는 소리 녹음
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP) // 출력 파일 포맷 설정
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) // 오디오 인코더 설정
            filePath = "${basePath}/myRecord.3gp"
            setOutputFile(filePath) //출력 파일 이름 설정
            Log.d("RecordStart", "start")
        }
        try {
            recorder?.prepare() //초기화 완료
        } catch (e: IOException) {
            Log.d("RecordFail", "prepare() failed")
            return
        }

        recorder?.start() //녹음 시작
        getDb() //데시벨 측정
    }

    //데시벨 측정
    private fun getDb() {
        recorder?.let {
            isRecording = true
            job = CoroutineScope(Dispatchers.Default).launch {
                while (isRecording) {
                    delay(2000L) //2초에 한번씩 데시벨을 측정
                    val amplitude = it.maxAmplitude
                    db = 20 * kotlin.math.log10(amplitude.toDouble()) //진폭 to 데시벨
                    Log.d("Record decibel", db.toString())
                    if (db.toInt() >= 85){
                        Log.d("Record decibel 50", "85")
                        onStop()
                    }
                    //데시벨은 기준 값을 기준으로 결정되는 것이라 한다.
                    //그래서 기준값을 넣고싶다면 아래와 같이 기준값으로 나눠주면 된다.
                    //val db = 20 * kotlin.math.log10(amplitude.toDouble()/기준값)
                    //아무것도 안 넣는다면 우리가 흔히 생각하는 데시벨값이 된다. > https://www.joongang.co.kr/article/23615791#home
                    if (amplitude > 0) {
                        changeDbText(db.toInt())
                        //진폭이 0 보다 크면 .. toDoSomething
                        //진폭이 0이하이면 데시벨이 -무한대로 나옵니다.
                    }
                }
            }
        }
    }

    //녹음 중지

    override fun onStop() {
        super.onStop()
        isRecording = false
        job?.cancel() //job cancle
        if (recorder != null) {
            recorder?.stop() // 녹음기 중지
            recorder?.release() //리소스 확보
            recorder = null
        }
        //completeDecibelCheck()
    }

    @SuppressLint("ResourceAsColor")
    fun changeDbText(db:Int){
        val dbValue = "$db db"
        val sps = SpannableStringBuilder(dbValue)
        val start = 0
        val end = db.toString().length
        val dbColor = R.color.color_main
        sps.setSpan(RelativeSizeSpan(2.0f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        sps.setSpan(ForegroundColorSpan(context?.resources?.getColor(dbColor) ?: Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 텍스트뷰에 저장합니다.
        dbText.text = sps
    }

}