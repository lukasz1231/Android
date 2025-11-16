package com.example.zadanie3

import androidx.fragment.app.Fragment


class TaskListActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment {
       return TaskListFragment()
    }
}