package com.dony.dramatest.data.local.dao

import androidx.room.*
import com.dony.dramatest.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity)

    @Query("SELECT * FROM UserEntity")
    fun loadAll(): MutableList<UserEntity>

    @Query("SELECT * FROM UserEntity where id = :id")
    fun getUser(id: Int): UserEntity

    @Delete
    fun deleteUser(user: UserEntity)

    @Query("DELETE FROM UserEntity")
    fun deleteAll()

    @Query("SELECT * FROM UserEntity WHERE login LIKE :login")
    fun searchUsers(login: String): MutableList<UserEntity>
}