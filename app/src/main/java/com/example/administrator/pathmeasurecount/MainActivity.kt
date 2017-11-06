package com.example.administrator.pathmeasurecount

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
        path.setOnClickListener {
            path.changeCommentLikeState()
        }
        button.setOnClickListener {
            path.setNumberWithAnimator(100999)
        }
        button2.setOnClickListener {
            path.subNumbers()
        }
    }
}
