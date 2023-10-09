package com.best.myappgithub.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.best.myappgithub.data.response.GithubResponse
import com.best.myappgithub.data.response.ItemsItem
import com.best.myappgithub.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listGitUser = MutableLiveData<List<ItemsItem>>()
    val User: LiveData<List<ItemsItem>> = _listGitUser

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        findUser("Rohman")
    }
    fun findUser(showListUser: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(showListUser)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listGitUser.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}