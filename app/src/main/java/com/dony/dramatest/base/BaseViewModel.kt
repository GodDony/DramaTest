package com.dony.dramatest.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 공통 에러 LiveData 변수 및 처리 함수
 * 각 화면 단위 에서 구독 후 처리.
 */
abstract class BaseViewModel : ViewModel() {
    val failureLiveData: MutableLiveData<Throwable> = MutableLiveData()

    protected fun handleFailure(throwable: Throwable) {
        failureLiveData.value = throwable
    }
}