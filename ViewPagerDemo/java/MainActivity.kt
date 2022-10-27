package org.anvei.viewpageruitest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var viewPager: ViewPager
    lateinit var view: View
    private lateinit var view1: View
    private lateinit var view2: View
    private lateinit var view3: View
    val views = ArrayList<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewpager)

        view1 = layoutInflater.inflate(R.layout.layout1, null)
        view2 = layoutInflater.inflate(R.layout.layout2, null)
        view3 = layoutInflater.inflate(R.layout.layout3, null)

        views.apply {
            add(view1)
            add(view2)
            add(view3)
        }

        pagerAdapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return views.size
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                container.addView(views[position])
                return views[position]
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(views[position])
            }
        }

        viewPager.adapter = pagerAdapter

        findViewById<Button>(R.id.left).setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}