package org.anvei.viewpageruitest

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class SecondActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var radioGroup: RadioGroup
    private var views = ArrayList<View>()

    private lateinit var tab1: RadioButton
    private lateinit var tab2: RadioButton
    private lateinit var tab3: RadioButton
    private lateinit var tab4: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        init()

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rd_message -> viewPager.currentItem = 0
                R.id.rd_contacts -> viewPager.currentItem = 1
                R.id.rd_find -> viewPager.currentItem = 2
                R.id.rd_me -> viewPager.currentItem = 3
            }
        }

    }

    private fun init() {
        viewPager = findViewById(R.id.viewpager_second)
        radioGroup = findViewById(R.id.radio)
        tab1 = findViewById(R.id.rd_message)
        tab2 = findViewById(R.id.rd_contacts)
        tab3 = findViewById(R.id.rd_find)
        tab4 = findViewById(R.id.rd_me)

        views.apply {
            add(layoutInflater.inflate(R.layout.find, null))
            add(layoutInflater.inflate(R.layout.me, null))
            add(layoutInflater.inflate(R.layout.contacts, null))
            add(layoutInflater.inflate(R.layout.message, null))
        }

        viewPager.adapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return views.size
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return `object` == view
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                container.addView(views[position])
                return views[position]
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
               container.removeView(views[position])
            }
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        tab1.isChecked = true
                        tab2.isChecked = false
                        tab3.isChecked = false
                        tab4.isChecked = false
                    }
                    1 -> {
                        tab1.isChecked = false
                        tab2.isChecked = true
                        tab3.isChecked = false
                        tab4.isChecked = false
                    }
                    2 -> {
                        tab1.isChecked = false
                        tab2.isChecked = false
                        tab3.isChecked = true
                        tab4.isChecked = false
                    }
                    3 -> {
                        tab1.isChecked = false
                        tab2.isChecked = false
                        tab3.isChecked = false
                        tab4.isChecked = true
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }
}