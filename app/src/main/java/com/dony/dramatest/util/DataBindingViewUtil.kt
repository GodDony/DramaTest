package com.dony.dramatest.util

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.dony.dramatest.extention.loadCircleImage
import com.dony.dramatest.extention.loadFromUrl
import com.dony.dramatest.extention.loadFromUrlCorner
import java.text.SimpleDateFormat
import java.util.*

object DataBindingViewUtil {
    @BindingAdapter("app:glide")
    @JvmStatic
    fun imageViewGlide(imageView: ImageView, uri: String?) {
        uri?.let {
            imageView.loadFromUrl(uri)
        }
    }

    @BindingAdapter("app:glideCorner")
    @JvmStatic
    fun imageViewGlideCorner(imageView: ImageView, uri: String?) {
        uri?.let {
            imageView.loadFromUrlCorner(uri)
        }
    }

    @BindingAdapter("app:glideCircle")
    @JvmStatic
    fun imageViewGlideCircle(imageView: ImageView, uri: String?) {
        uri?.let {
            imageView.loadCircleImage(uri)
        }
    }

    @BindingAdapter("app:imgRes")
    @JvmStatic
    fun imageLoad(imageView: ImageView, resId: Int) {
        imageView.setImageResource(resId)
    }

    @SuppressLint("SimpleDateFormat")
    @BindingAdapter("app:simpleDateText")
    @JvmStatic
    fun simpleDateText(textView: TextView, date: Date) {
        textView.text = SimpleDateFormat("yyyy.MM.dd ").format(date)
    }
}