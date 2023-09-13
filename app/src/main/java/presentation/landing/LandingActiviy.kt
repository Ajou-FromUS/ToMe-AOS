package presentation.landing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.tome_aos.databinding.ActivityLandingBinding
import presentation.login.LoginActivity

class LandingActiviy : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var slideText: TextView
    private lateinit var startButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.vpLanding
        slideText = binding.tvSlide
        startButton = binding.btnStartTome

        val viewPagerAdapter = ViewpagerFragmentAdapter(this)
        viewPager.adapter = viewPagerAdapter
        binding.dotIndicator.attachTo(viewPager)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position == 3 && positionOffset > 1 || position == 4) {
                    slideText.visibility = View.INVISIBLE
                    startButton.visibility = View.VISIBLE
                }
                else{
                    slideText.visibility = View.VISIBLE
                    startButton.visibility = View.INVISIBLE
                }
            }
        })
        startButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private inner class ViewpagerFragmentAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        val fragmentList = listOf<Fragment>(
            FirstFragment(),
            SecondFragment(),
            ThirdFragment(),
            FourthFragment(),
            FifthFragment()
        )

        override fun getItemCount(): Int {
            return fragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }
    }
}