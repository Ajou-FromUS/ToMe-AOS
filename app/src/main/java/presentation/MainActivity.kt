package presentation

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import application.ApplicationClass
import com.example.tome_aos.R
import com.example.tome_aos.databinding.ActivityMainBinding
import data.dto.response.InitResponse
import data.service.ApiClient
import data.service.InitClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import presentation.chat.ChatActivity
import presentation.home.HomeFragment
import presentation.login.LoginWebviewActivity
import presentation.mypage.MypageFragment
import presentation.statistics.StatisticsFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val mypageFragment = MypageFragment()
    private val statisticsFragment = StatisticsFragment()
    private lateinit var currentFragment: Fragment
    private lateinit var nickNameText: TextView
    
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getInit()

        binding.bnvMain.selectedItemId = R.id.navi_home
        nickNameText = binding.mainTitleText

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

    }

    private fun setBundle(hasMission: Boolean){
        val bundle = Bundle()
        bundle.putBoolean("hasMission", hasMission)
        Log.d("main has Mission", hasMission.toString())
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
                        binding.mainTitleText.text = "${initData.nickname},\n오늘도 나랑 대화하자"
                        setBundle(initData.has_mission_today)
                    } else {
                        Log.d("initResponse", "HTTP 오류")
                    }
                }
                override fun onFailure(call: Call<InitResponse>, t: Throwable) {
                    Log.d("initFail", t.toString())
                    t.printStackTrace()
                }
            })
        }
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frameLayout, fragment)
        transaction.commit()

        if(fragment != homeFragment){
            binding.topBarLayout.visibility = View.GONE
        }else{
            binding.topBarLayout.visibility = View.VISIBLE
        }
    }

    private fun showActivity(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }

    fun hideBottomNavigation(state:Boolean){
        if(state) {
            binding.bnvMain.visibility = View.GONE
        }else{
            binding.bnvMain.visibility = View.VISIBLE
        }
    }

    fun changeMainTitle(pageNumber: Int, nickName: String?){
        when(pageNumber){
            0 -> binding.mainTitleText.setText("${nickName},\n오늘도 나랑 대화하자.")
            1 -> binding.mainTitleText.setText(R.string.title_mission)
        }
    }
}