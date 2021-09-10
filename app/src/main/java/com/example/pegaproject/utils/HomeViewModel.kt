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

    private val _stockLiveData = MutableLiveData<StockApiResponse>()
    val stockLiveData: LiveData<StockApiResponse> = _stockLiveData
    var serviceStarted = false
    private var subscription: Disposable? = null


    fun getFavStock() {
        if (!serviceStarted) {
            subscription = Observable.interval(0, 3, TimeUnit.SECONDS)
                .flatMap { Repository.fetchFavStock() }
                .scan { t1, t2 ->
                    t2.map.filterKeys { s -> t1.map.containsKey(s) }
                        .forEach { (s,key) ->
                            if(t1.map.getValue(s).price > key.price){
                                key.color = "red"
                            }
                            if(t1.map.getValue(s).price < key.price){
                                key.color = "green"
                            }
                            if(t1.map.getValue(s).price == key.price){
                                key.color = "grey"
                            }
                        }
                    t2
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onError = {
                    val temp = StockApiResponse(Status.ERROR, mapOf())
                    _stockLiveData.value = temp
                }, onNext = { it ->
                    it.status = Status.SUCCESS
                    _stockLiveData.value = it
                })
        }
        serviceStarted = true
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }
}