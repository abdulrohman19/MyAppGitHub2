package com.best.myappgithub.data.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.best.myappgithub.data.FavoriteRepo
import com.best.myappgithub.data.database.FavoriteUser

class FavoriteViewModel (application: Application): ViewModel(){
    private val mFavoriteRepository: FavoriteRepo = FavoriteRepo(application)

    fun getAllFavorite(): LiveData<List<FavoriteUser>> = mFavoriteRepository.getAllFavorite()}