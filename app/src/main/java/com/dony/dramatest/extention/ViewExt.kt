package com.dony.dramatest.extention

import android.app.Service
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

fun View.showKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.isGone(): Boolean {
    return this.visibility == View.GONE
}

fun ImageView.loadFromUrl(url: String) =
    Glide.with(this.context.applicationContext)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)

fun ImageView.loadFromUrlCorner(url: String) =
    Glide.with(this.context.applicationContext)
        .load(url)
        .apply(
            RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(Target.SIZE_ORIGINAL)
                .dontAnimate().transform(RoundedCorners(12))
        )
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)

fun ImageView.loadCircleImage(url: String) =
    Glide.with(this.context.applicationContext)
        .load(url)
        .apply(
            RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(Target.SIZE_ORIGINAL)
                .dontAnimate().circleCrop()
        )
        .into(this)

/**
 * 텍스트 부분 색상 변경
 */
fun TextView.textSubColor(fulltext: String, subtext: String, color: Int) {
    SpannableStringBuilder(fulltext).run {
        setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this@textSubColor.context, color)),
            fulltext.indexOf(subtext), fulltext.indexOf(subtext) + subtext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        this@textSubColor.text = this
    }
}