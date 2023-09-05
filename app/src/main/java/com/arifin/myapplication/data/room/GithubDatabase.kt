package com.arifin.myapplication.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.arifin.myapplication.data.entity.GitHubEntity

@Database(entities = [GitHubEntity::class], version = 2, exportSchema = false)
abstract class GitHubDatabase : RoomDatabase() {
    abstract fun githubDao(): GitHubDao

    companion object {
        @Volatile
        private var instance: GitHubDatabase? = null
        fun getInstance(context: Context): GitHubDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    GitHubDatabase::class.java, "Github.db"
                ).addMigrations(MIGRATION_1_2).build()
            }

        // Definisikan migrasi dari versi 1 ke versi 2
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE github ADD COLUMN isBookmarked INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}

