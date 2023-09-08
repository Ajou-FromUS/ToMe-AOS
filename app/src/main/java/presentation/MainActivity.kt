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

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.top_bar_linear, TopBarFragment())
        transaction.commit()

        binding.bnvMain.itemIconTintList = null
        replaceBnvFragment(HomeFragment())

        binding.bnvMain.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navi_home -> {
                    replaceBnvFragment(HomeFragment())
                }
                R.id.navi_archive -> {
                    replaceBnvFragment(ArchiveFragment())
                }
                R.id.navi_diary -> {
                    replaceBnvFragment(DiaryFragment())
                }
                R.id.navi_chat -> {
                    replaceBnvFragment(ChatFragment())
                }
            }
            true
        }
    }
    private fun replaceBnvFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.main_frameLayout, fragment)
                commit()
            }
    }
}