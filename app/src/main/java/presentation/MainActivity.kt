package presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tome_aos.R
import com.example.tome_aos.databinding.ActivityMainBinding
import presentation.archive.ArchiveFragment
import presentation.chat.ChatFragment
import presentation.diary.DiaryFragment
import presentation.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val archiveFragment = ArchiveFragment()
    private val diaryFragment = DiaryFragment()
    private val chatFragment = ChatFragment()
    private lateinit var currentFragment: Fragment


    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.top_bar_linear, TopBarFragment())
        transaction.commit()

        binding.bnvMain.itemIconTintList = null

        currentFragment = homeFragment
        supportFragmentManager.beginTransaction()
            .add(R.id.main_frameLayout, homeFragment)
            .commit()
        binding.bnvMain.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navi_home -> {
                    showFragment(homeFragment)
                }

                R.id.navi_archive -> {
                    showFragment(archiveFragment)
                }

                R.id.navi_diary -> {
                    showFragment(diaryFragment)
                }

                R.id.navi_chat -> {
                    showFragment(chatFragment)
                }
            }
            true
        }
//        replaceBnvFragment(HomeFragment())
//
//        binding.bnvMain.setOnItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.navi_home -> {
//                    replaceBnvFragment(HomeFragment())
//                }
//                R.id.navi_archive -> {
//                    replaceBnvFragment(ArchiveFragment())
//                }
//                R.id.navi_diary -> {
//                    replaceBnvFragment(DiaryFragment())
//                }
//                R.id.navi_chat -> {
//                    replaceBnvFragment(ChatFragment())
//                }
//            }
//            true
//        }
    }
    private fun replaceBnvFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.main_frameLayout, fragment)
                commit()
            }
    }

    private fun replaceTopBarName(){
    }

    private fun showFragment(fragment: Fragment) {
        if (fragment != currentFragment) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.hide(currentFragment)
            if (!fragment.isAdded) {
                transaction.add(R.id.main_frameLayout, fragment)
            } else {
                transaction.show(fragment)
            }
            transaction.commit()
            currentFragment = fragment
        }
    }
}