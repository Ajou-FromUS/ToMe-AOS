package presentation.landing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.tome_aos.databinding.ActivityLandingBinding

class LandingActiviy : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var slideText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.vpLanding
        slideText = binding.tvSlide

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
                // 페이지 스크롤 중에 호출됩니다.
                // 다음 페이지로 슬라이드하는 순간에 텍스트 뷰를 숨깁니다.
                if (position == 3 && positionOffset > 0.1 || position == 4) {
                    slideText.visibility = View.INVISIBLE
                }
                else{
                    slideText.visibility = View.VISIBLE
                }
            }
        })
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