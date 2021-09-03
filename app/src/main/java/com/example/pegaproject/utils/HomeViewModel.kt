package com.example.pegaproject.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pegaproject.model.Status
import com.example.pegaproject.model.StockApiResponse
import com.example.pegaproject.backend.Repository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit

class HomeViewModel : ViewModel() {

    private val _stockLiveData: MutableLiveData<StockApiResponse> = MutableLiveData()
    val stockLiveData: LiveData<StockApiResponse> = _stockLiveData
    var serviceStarted = false
    private var subscription: Disposable? = null

    fun getFavStock() {
        if (!serviceStarted) {
            subscription = Observable.interval(0, 10, TimeUnit.SECONDS)
                .flatMap { Repository.fetchFavStock() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onError = {
                    val temp = StockApiResponse(Status.ERROR, null)
                    _stockLiveData.value = temp
                }, onNext = {
                    val temp = StockApiResponse(Status.SUCCESS, it.map)
                    _stockLiveData.value = temp
                })
        }
        serviceStarted = true
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }
}