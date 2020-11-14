package io.github.taowata.engineerlevel

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["android:text"], requireAll = false)
fun setIntAsText(textView: TextView, number: Int) {
    textView.text = number.toString()
}