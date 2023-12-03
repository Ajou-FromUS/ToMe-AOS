package presentation

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tome_aos.R
import com.example.tome_aos.databinding.ActivityMainBinding
import presentation.chat.ChatActivity
import presentation.home.HomeFragment
import presentation.login.LoginWebviewActivity
import presentation.mypage.MypageFragment
import presentation.statistics.StatisticsFragment
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

        binding.bnvMain.selectedItemId = R.id.navi_home

        changeMainTitle(0)
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
        findTag()
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

    fun changeMainTitle(pageNumber: Int){
        when(pageNumber){
            0 -> binding.mainTitleText.setText(R.string.exam_main_text)
            1 -> binding.mainTitleText.setText(R.string.title_mission)
        }
    }
    fun findTag() {
        val fragmentManager = supportFragmentManager

        val onBackStackChangedListener = FragmentManager.OnBackStackChangedListener {
            val currentFragment = fragmentManager.findFragmentById(R.id.frame_mypage)
            val currentTag = currentFragment?.tag
            println(currentTag)
            if(currentTag == "MISSION_CHECK" || currentTag == "QNA" ||
                currentTag == "ACCOUNT_SETTING" || currentTag == "NOTIFICATION") {
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