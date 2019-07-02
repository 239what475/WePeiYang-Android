package com.twt.service.theory.view

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Window
import android.widget.LinearLayout
import com.twt.service.theory.R
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.listener.OnBannerListener
import kotlinx.android.synthetic.main.theory_common_toolbar.*
import org.jetbrains.anko.startActivity

class TheoryActivity : AppCompatActivity(), OnBannerListener {
    override fun OnBannerClick(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var list: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.theory_activity_main)
        theory_person_profile.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
        theory_back.setOnClickListener {
            finish()
        }
        theory_search.setOnClickListener {

        }
        window.statusBarColor = Color.parseColor("#FFFFFF")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        list.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg")
        list.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg")
        val banner = findViewById<Banner>(R.id.main_banner)
        banner.apply {
            setImageLoader(GlideImageLoader())
            setImages(list)

            this.start()
        }


        val theoryTabLayout: TabLayout = findViewById(R.id.main_tablayout)
        val theoryViewPager: ViewPager = findViewById(R.id.main_viewpager)
        val myhomePagerAdapter = TheoryPagerAdapter(supportFragmentManager)

        myhomePagerAdapter.apply {
            add(ExamFragment(), "考试")
            add(MessageFragement(), "通知")
        }
        theoryViewPager.adapter = myhomePagerAdapter
        theoryTabLayout.apply {
            setupWithViewPager(theoryViewPager)
            tabGravity = TabLayout.GRAVITY_FILL
            setSelectedTabIndicatorColor(Color.parseColor("#1E90FF"))
        }

    }
}
