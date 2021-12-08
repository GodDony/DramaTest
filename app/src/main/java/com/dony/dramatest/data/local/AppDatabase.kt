package com.dony.dramatest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dony.dramatest.data.local.dao.UserDao
import com.dony.dramatest.data.local.entity.UserEntity
import com.dony.dramatest.util.DataConverter

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        const val DB_NAME = "github.db"
    }
}