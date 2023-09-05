package com.arifin.myapplication.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arifin.myapplication.data.entity.GitHubEntity

@Dao
interface GitHubDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGitHubUser(gitHubUser: GitHubEntity)

    @Query("SELECT * FROM github WHERE username = :username")
    fun getGitHubUserByUsername(username: String): GitHubEntity?

    @Query("DELETE FROM github WHERE username = :username")
    fun deleteGitHubUserByUsername(username: String)

    @Query("SELECT * FROM github WHERE isBookmarked = 0")
    fun getBookmarkedGitHubUsers(): LiveData<List<GitHubEntity>>

    @Query("SELECT EXISTS(SELECT * FROM github WHERE username = :username AND isBookmarked = 0)")
    fun isGitHubUserBookmarked(username: String): Boolean
}

