package com.best.myappgithub.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.best.myappgithub.data.database.FavoriteDao
import com.best.myappgithub.data.database.FavoriteRoomDatabase
import com.best.myappgithub.data.database.FavoriteUser
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepo (application: Application) {
    private val mFavoriteDao: FavoriteDao

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorite(): LiveData<List<FavoriteUser>> = mFavoriteDao.getFav()

    fun insert(favorite: FavoriteUser) {
        executorService.execute { mFavoriteDao.insertFav(favorite) }
    }

    fun delete(favorite: FavoriteUser) {
        executorService.execute { mFavoriteDao.deleteFav(favorite) }
    }

}