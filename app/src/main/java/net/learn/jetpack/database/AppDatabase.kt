package net.learn.jetpack.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.learn.jetpack.model.tv.TvShow
import net.learn.submission4mvvm.model.movies.Movie

@Database(entities = [Movie::class, TvShow::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvDao(): TvDao

    companion object {
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            instance?.let { return it }
            instance = Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java,
                "db_apps.db"
            ).build()
            return instance!!
        }
    }

}