package presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import application.ApplicationClass
import android.graphics.Rect
import android.view.MotionEvent
import androidx.fragment.app.FragmentManager
import com.example.tome_aos.R
import com.example.tome_aos.databinding.ActivityMainBinding
import data.dto.response.InitResponse
import data.service.ApiClient
import data.service.InitClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import presentation.home.HomeFragment
import presentation.mypage.MypageFragment
import presentation.statistics.StatisticsFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import util.MusicService
import util.hideKeyboard

class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val mypageFragment = MypageFragment()
    private val statisticsFragment = StatisticsFragment()
    private lateinit var currentFragment: Fragment
    
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getInit()
        ServiceStart(binding.root)

        binding.bnvMain.selectedItemId = R.id.navi_home

        binding.bnvMain.itemIconTintList = null
        currentFragment = homeFragment

        binding.bnvMain.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navi_home -> showFragment(homeFragment)
                R.id.navi_statistics -> showFragment(statisticsFragment)
                R.id.navi_mypage -> showFragment(mypageFragment)
            }
            true
        }
        findTag()
    }

    fun ServiceStart(view : View){
        val intent = Intent(this,MusicService::class.java)
        startService(intent) // 서비스 시작
    }

    private fun setBundle(hasMission: Boolean, nickName: String?){
        val bundle = Bundle()
        bundle.putString("nickname", nickName)
        bundle.putBoolean("hasMission", hasMission)
        homeFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .add(R.id.main_frameLayout, homeFragment)
            .commit()
    }

    private fun getInit() {
        val client = ApiClient.getApiClient().create(InitClient::class.java)
        lifecycleScope.launch {
            val accessToken = ApplicationClass.getInstance().getDataStore().accessToken.first()
            val refreshToken = ApplicationClass.getInstance().getDataStore().refreshToken.first()
            client.getInit(accessToken, refreshToken).enqueue(object : Callback<InitResponse> {
                override fun onResponse(call: Call<InitResponse>, response: Response<InitResponse>) {
                    if (response.isSuccessful) {
                        Log.d("initResponse", response.body().toString())
                        val initData: InitResponse.Data = response.body()!!.initData
                        setBundle(initData.has_mission_today, initData.nickname)
                    } else {
                        Log.d("fail initResponse", "HTTP 오류")
                    }
                }
                override fun onFailure(call: Call<InitResponse>, t: Throwable) {
                    Log.d("initFail", t.toString())
                }
            })
        }
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frameLayout, fragment)
        transaction.commit()
    }

    fun hideBottomNavigation(state:Boolean){
        if(state) {
            binding.bnvMain.visibility = View.GONE
        }else{
            binding.bnvMain.visibility = View.VISIBLE
        }
    }
    fun findTag() {
        val fragmentManager = supportFragmentManager

        val onBackStackChangedListener = FragmentManager.OnBackStackChangedListener {
            val currentFragment = fragmentManager.findFragmentById(R.id.frame_mypage)
            val missionFragment = fragmentManager.findFragmentById(R.id.main_frameLayout)
            val currentTag = currentFragment?.tag
            val missionTag = missionFragment?.tag
            println(currentTag)
            if(currentTag == "MISSION_CHECK" || currentTag == "QNA" ||
                currentTag == "ACCOUNT_SETTING" || currentTag == "NOTIFICATION" || missionTag == "MISSION") {
                hideBottomNavigation(true)
            } else {
                hideBottomNavigation(false)
            }
        }
        fragmentManager.addOnBackStackChangedListener(onBackStackChangedListener)
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev!!.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                hideKeyboard(focusView)
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}