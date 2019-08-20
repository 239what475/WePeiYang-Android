package com.twt.service.theory.view

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.twt.service.theory.R
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.ui.rec.withItems
import kotlinx.android.synthetic.main.theory_activity_answer.*
import kotlinx.android.synthetic.main.theory_common_toolbar.*
import kotlinx.android.synthetic.main.theory_popupwindow_layout.view.*
import org.jetbrains.anko.dip

class AnswerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.theory_activity_answer)
        initView()
        loadQuestions()
//        AnswerManager.init(58)
    }


    private fun initView() {
        window.statusBarColor = Color.parseColor("#FFFFFF")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        enableLightStatusBarMode(true)
        theory_submit.visibility = View.VISIBLE
        theory_person_profile.visibility = View.GONE
        theory_search.visibility = View.GONE
        theory_back.setOnClickListener {
            finish()
        }
        constraintLayout.setOnClickListener {
            val popupWindow = PopupWindow(this)
            val view = LayoutInflater.from(this).inflate(R.layout.theory_popupwindow_layout, null, false)
            popupWindow.contentView = view
            popupWindow.width = WindowManager.LayoutParams.MATCH_PARENT
            popupWindow.animationStyle = R.style.style_pop_animation
            popupWindow.setBackgroundDrawable(null)
            popupWindow.height = dip(280)
            popupWindow.showAtLocation(
                    LayoutInflater.from(this).inflate(R.layout.theory_activity_answer, null),
                    Gravity.BOTTOM,
                    0,
                    0
            )
            popupWindow.contentView.constraintLayout2.setOnClickListener {
                popupWindow.dismiss()
                AnswerManager.uninstall()
            }
            popupWindow.contentView.recyclerView.layoutManager = GridLayoutManager(this, 8, GridLayoutManager.VERTICAL, false)
            AnswerManager.installPopUpWindow(popupWindow)
        }
    }

    private fun loadQuestions() {
        val recyclerView = findViewById<RecyclerView>(R.id.theory_exam_questions)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.withItems {
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
        }

    }

}
