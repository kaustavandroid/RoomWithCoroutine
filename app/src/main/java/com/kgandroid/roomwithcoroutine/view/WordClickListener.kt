package com.kgandroid.roomwithcoroutine.view

import android.view.View
import com.kgandroid.roomwithcoroutine.data.Word

interface WordClickListener {

    fun onclickWord (v : View , word :Word)
}