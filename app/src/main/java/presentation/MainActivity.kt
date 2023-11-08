package presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.tome_aos.R
import com.example.tome_aos.databinding.ActivityMainBinding
import presentation.archive.ArchiveFragment
import presentation.diary.DiaryFragment
import presentation.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val archiveFragment = ArchiveFragment()
    private val diaryFragment = DiaryFragment()
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
                R.id.navi_home -> {
                    showFragment(homeFragment)
                }
                R.id.navi_archive -> {
                    showFragment(archiveFragment)
                }
                R.id.navi_mypage -> {
                    showFragment(diaryFragment)
                }
            }
            true
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
}