package com.android.exercise.ui.activity.android11

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.exercise.R
import com.android.exercise.base.BaseActivity

/**
 * 自定义消息框视图被屏蔽
 * Created by wangzhen on 2020/8/26.
 */
class ToastCompatActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toast_compat)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_normal_toast -> {
                Handler(Looper.getMainLooper()).postDelayed({
                    Toast.makeText(this, "延时3秒普通Toast", Toast.LENGTH_SHORT).show()
                }, 3000)
            }
            R.id.btn_custom_toast -> {
                Handler(Looper.getMainLooper()).postDelayed({
                    val toast = Toast(this)
                    val layout = View.inflate(this, R.layout.toast_custom_layout, null)
                    layout.findViewById<TextView>(R.id.tv).text = "延时3秒自定义Toast"
                    toast.view = layout
                    toast.duration = Toast.LENGTH_SHORT
                    toast.show()
                }, 3000)
            }
        }
    }
}