package com.best.myappgithub.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFav(favorite: FavoriteUser)

    @Delete
    fun deleteFav(favorite: FavoriteUser)

    @Query("SELECT * from favoriteuser ORDER BY name_user ASC")
    fun getFav(): LiveData<List<FavoriteUser>>
}