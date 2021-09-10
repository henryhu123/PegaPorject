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
            subscription = Observable.interval(0, 3, TimeUnit.SECONDS)
                .flatMap { Repository.fetchFavStock() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onError = {
                    val temp = StockApiResponse(Status.ERROR, null)
                    _stockLiveData.value = temp
                }, onNext = {
                    val prevMap = _stockLiveData.value?.map
                    it.status = Status.SUCCESS
                    if(it.map !=null){
                        for(key:String in it.map.keys){
                            if(prevMap != null && it.map != null){
                                if(prevMap[key]!!.price > it.map?.get(key)!!.price){
                                    it.map.get(key)!!.color = "red"
                                }
                                if(prevMap[key]!!.price < it.map?.get(key)!!.price){
                                    it.map.get(key)!!.color = "green"
                                }
                                if(prevMap[key]!!.price == it.map?.get(key)!!.price){
                                    it.map.get(key)!!.color = "grey"
                                }
                            }
                        }
                    }
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