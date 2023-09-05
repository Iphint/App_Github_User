package com.arifin.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.arifin.myapplication.R
import com.arifin.myapplication.data.room.GitHubDatabase
import com.arifin.myapplication.data.room.GitHubDao
import com.arifin.myapplication.databinding.ActivityFavouriteBinding
import com.arifin.myapplication.data.entity.GitHubEntity

class FavouriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouriteBinding
    private lateinit var githubDatabase: GitHubDatabase
    private lateinit var githubDao: GitHubDao
    private lateinit var favouriteAdapter: FavouriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        val inf = binding.root
        setContentView(inf)

        githubDatabase = GitHubDatabase.getInstance(this)
        githubDao = githubDatabase.githubDao()

        favouriteAdapter = FavouriteAdapter { gitHubUser ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("username", gitHubUser.username)
            startActivity(intent)
        }
        binding.recyclerView2.layoutManager = LinearLayoutManager(this)
        binding.recyclerView2.adapter = favouriteAdapter

        binding.btnBack.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        val jmlFav = findViewById<TextView>(R.id.jmlFav)

        githubDao.getBookmarkedGitHubUsers().observe(this, Observer { favouriteUsers ->

            favouriteAdapter.setData(favouriteUsers)

            val jumlahFav = favouriteUsers.size
            val kalimatFav = "Total Favorite: $jumlahFav"

            jmlFav.text = kalimatFav

            if (favouriteUsers.isEmpty()) {
                binding.notFound.visibility = View.VISIBLE
                Log.d("FavouriteActivity", "Favourite users are empty")
            } else {
                binding.notFound.visibility = View.GONE
                Log.d("FavouriteActivity", "Favourite users found: ${favouriteUsers.size}")
            }
        })
    }
}
