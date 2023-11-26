package presentation

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.tome_aos.R
import com.example.tome_aos.databinding.ActivityMainBinding
import presentation.chat.ChatActivity
import presentation.home.HomeFragment
import presentation.login.LoginWebviewActivity
import presentation.mypage.MypageFragment
import presentation.statistics.StatisticsFragment

class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val mypageFragment = MypageFragment()
    private val statisticsFragment = StatisticsFragment()
    private lateinit var currentFragment: Fragment


    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bnvMain.selectedItemId = R.id.navi_home

        binding.bnvMain.itemIconTintList = null
        currentFragment = homeFragment

        supportFragmentManager.beginTransaction()
            .add(R.id.main_frameLayout, homeFragment)
            .commit()

        binding.bnvMain.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navi_home -> showFragment(homeFragment)
                R.id.navi_statistics -> showFragment(statisticsFragment)
                R.id.navi_mypage -> showFragment(mypageFragment)
            }
            true
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