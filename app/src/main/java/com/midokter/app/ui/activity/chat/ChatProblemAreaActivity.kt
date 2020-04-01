package com.midokter.app.ui.activity.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityChatProblemAreaBinding
import com.midokter.app.ui.adapter.ChatProblemAreasListAdapter
import kotlinx.android.synthetic.main.activity_chat_problem_area.*

class ChatProblemAreaActivity : BaseActivity<ActivityChatProblemAreaBinding>() {

    val problemAreas: ArrayList<String> = ArrayList()


    override fun getLayoutId(): Int = R.layout.activity_chat_problem_area

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        addProblemAreas()
        // Creates a vertical Layout Manager
        rv_chat_problem.layoutManager = GridLayoutManager(applicationContext,2)

        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
//        rv_animal_list.layoutManager = GridLayoutManager(this, 2)

        // Access the RecyclerView Adapter and load the data into it
        rv_chat_problem.adapter = applicationContext?.let { ChatProblemAreasListAdapter(problemAreas, it) }
    }

    private fun addProblemAreas() {
        problemAreas.add("General Physician")
        problemAreas.add("Gynaecology")
        problemAreas.add("Dermatology")
        problemAreas.add("Eye & Vision")
        problemAreas.add("Sexology")
        problemAreas.add("Psychiatry")
        problemAreas.add("Diet & Nutrition")
        problemAreas.add("Stomuch & Digestion")
        problemAreas.add("Breathing & Chest")
        problemAreas.add("Cancer")
        problemAreas.add("Kidney & Urine")
        problemAreas.add("Orthopedic")
        problemAreas.add("Diabetes")
    }
}
