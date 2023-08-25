package presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tome_aos.R
import com.example.tome_aos.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import presentation.archive.ArchiveFragment
import presentation.chat.ChatFragment
import presentation.diary.DiaryFragment
import presentation.home.HomeFragment

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bnvMain.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navi_home -> {
                    replaceFragment(HomeFragment())
                }
                R.id.navi_archive -> {
                    replaceFragment(ArchiveFragment())
                }
                R.id.navi_diary -> {
                    replaceFragment(DiaryFragment())
                }
                R.id.navi_chat -> {
                    replaceFragment(ChatFragment())
                }
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.main_frameLayout, fragment)
                commit()
            }
    }
}