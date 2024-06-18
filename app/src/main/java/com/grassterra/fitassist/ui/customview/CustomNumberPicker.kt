package com.grassterra.fitassist.ui.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.EditText
import android.widget.NumberPicker

class CustomNumberPicker(context: Context, attrs: AttributeSet) : NumberPicker(context, attrs) {
    init {
        setTextColor(Color.WHITE)
    }

    override fun setTextColor(color: Int) {
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child is EditText) {
                child.setTextColor(color)
            }
        }
    }
}
