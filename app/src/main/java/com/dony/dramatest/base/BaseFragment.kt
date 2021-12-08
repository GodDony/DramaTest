package com.dony.dramatest.base

import android.transition.TransitionManager
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.dony.dramatest.data.remote.response.ErrorResponse
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.HttpException

open class BaseFragment : Fragment() {
    fun String.showToast() {
        Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT).show()
    }

    fun Int.showToast() {
        Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT).show()
    }

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

    /**
     * 에러 핸들링
     */
    protected fun onErrorHandle(throwable: Throwable, errorUnit: ((value: ErrorResponse) -> Unit)? = null) {
        Log.d("Erorr", "throwable: ${throwable.stackTraceToString()}")
        onCommonRemoteError(throwable).subscribe { error ->
            when (error.code) {
                401 -> {
                    //TODO 인증 에러 별도 처리 필요
                }
                else -> {
                    errorUnit?.invoke(error)
                }
            }
        }
    }

    /**
     * 네트워크 공통 에러 처리
     */
    private fun onCommonRemoteError(throwable: Throwable): Single<ErrorResponse> {
        lateinit var errorModel: ErrorResponse
        when (throwable) {
            is HttpException -> {
                // Error Response
                val statusCode = throwable.response()?.code()
                var errorMessage = ""
                var errorType = ""
                JSONObject(throwable.response()?.errorBody()?.string()).run {
                    errorMessage = getString("message")
                    errorType = getString("errorType")
                }
                errorModel = ErrorResponse(code = statusCode ?: 400, message = errorMessage, type = errorType)
            }
            else -> errorModel = ErrorResponse(code = 400, message = "", type = "")
        }
        return Single.just(errorModel)
    }
}

