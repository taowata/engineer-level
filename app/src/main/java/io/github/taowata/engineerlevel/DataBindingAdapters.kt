package io.github.taowata.engineerlevel

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter(value = ["android:text"], requireAll = false)
fun setIntAsText(textView: TextView, number: Int) {
    textView.text = number.toString()
}

@BindingAdapter("profileImage")
fun loadImage(view: ImageView, imageUrl: String?) {

    //imageUrlはnullチェックが必要です。
    if(imageUrl != null) {
        Glide.with(view.context)
            .load(imageUrl).apply(RequestOptions().circleCrop())
            .into(view)
    }
}

