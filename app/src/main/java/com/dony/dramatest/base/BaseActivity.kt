package com.dony.dramatest.base

import android.transition.TransitionManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

open class BaseActivity : AppCompatActivity() {

    /**
     * ConstraintLayout 애니메이션 효과 적용.
     */
    protected fun beginDelayedTransition(constraintLayout: ConstraintLayout) {
        ConstraintSet().apply {
            clone(constraintLayout)
            TransitionManager.beginDelayedTransition(constraintLayout)
            applyTo(constraintLayout)
        }
    }
}