package org.anvei.widgettest

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        popup.setOnClickListener {
            val contentView: View = LayoutInflater.from(this).inflate(R.layout.pop_window_item, null)
            val popupWindow = PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true)
            with(popupWindow) {
                showAtLocation(it, Gravity.BOTTOM, 0, 0)
            }
            contentView.findViewById<Button>(R.id.popup_cancel).setOnClickListener {
                popupWindow.dismiss()
            }
            contentView.findViewById<Button>(R.id.popup_select).setOnClickListener {
                Toast.makeText(this, "select photo", Toast.LENGTH_SHORT).show()
            }
            contentView.findViewById<Button>(R.id.popup_take_photo).setOnClickListener {
                Toast.makeText(this, "take photo", Toast.LENGTH_SHORT).show()
            }
        }

//        val floatingButton = Button(this)
//        floatingButton.text = "FloatingButton"
//        val layoutParams = WindowManager.LayoutParams()
//
//        layoutParams.apply {
//            height = WindowManager.LayoutParams.WRAP_CONTENT
//            width = WindowManager.LayoutParams.WRAP_CONTENT
//            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
//                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//            gravity = Gravity.START or Gravity.TOP
//            x = 100
//            y = 300
//        }
//        (getSystemService(Context.WINDOW_SERVICE) as WindowManager).addView(floatingButton, layoutParams)

    }
}