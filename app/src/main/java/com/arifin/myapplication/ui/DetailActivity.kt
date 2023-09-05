package com.arifin.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.arifin.myapplication.R
import com.arifin.myapplication.data.entity.GitHubEntity
import com.arifin.myapplication.data.room.GitHubDatabase
import com.arifin.myapplication.data.room.GitHubDao
import com.arifin.myapplication.data.retrofit.ApiConfig
import com.arifin.myapplication.data.response.DetailUserResponse
import com.arifin.myapplication.databinding.ActivityDetailBinding
import com.arifin.myapplication.ui.fragment.SectionsPagerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following,
        )
    }

    private var isBookmarked = false

    private lateinit var binding: ActivityDetailBinding
    private lateinit var githubDatabase: GitHubDatabase
    private lateinit var githubDao: GitHubDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username") ?: ""

        supportActionBar?.hide()

        githubDatabase = GitHubDatabase.getInstance(this)
        githubDao = githubDatabase.githubDao()

        checkIsBookmarked(username)

        val apiService = ApiConfig.getApiServices()
        val call = apiService.getDetailUser(username)

        call.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {

                    binding.favourite.setOnClickListener {
                        if (isBookmarked) {
                            removeBookmark(username)
                        } else {
                            val user = response.body()
                            val avatarUrl = user?.avatarUrl ?: ""
                            addBookmark(username, avatarUrl)
                        }
                    }

                    val user = response.body()

                    binding.name.text = user?.name
                    binding.username.text = user?.login
                    binding.bio.text = user?.bio as CharSequence?

                    val totalFollowers = user?.followers ?: 0
                    val totalFollowing = user?.following ?: 0

                    binding.followers.text = "${totalFollowers} Followers"
                    binding.following.text = "${totalFollowing} Following"

                    Glide.with(this@DetailActivity)
                        .load(user?.avatarUrl)
                        .placeholder(R.drawable.baseline_error_24)
                        .error(R.drawable.baseline_error_24)
                        .into(binding.imageView)

                    setupViewPager(username)
                } else {
                    val errorMessage = "Error : ${response.code()} - ${response.message()}"
                    binding.errorText.text = errorMessage
                    binding.errorText.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                val errorMessage = "Network Error : ${t.message}"
                binding.errorText.text = errorMessage
                binding.errorText.visibility = View.VISIBLE
            }
        })

        binding.btnBack.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    private fun setupViewPager(username: String) {

        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        val viewPager: ViewPager2 = binding.tampilan
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun addBookmark(username: String, avatarUrl: String) {
        isBookmarked = true
        binding.favourite.setImageResource(R.drawable.baseline_favorite_24)

        CoroutineScope(Dispatchers.IO).launch {
            // Tambahkan item ke database
            val gitHubUser = GitHubEntity(username, avatarUrl)
            githubDao.insertGitHubUser(gitHubUser)
            Log.d("FavouriteActivity", "Telah di tambahkan ke favorit yaitu : $username dan juga URL image $avatarUrl")
        }
    }

    private fun removeBookmark(username: String) {
        isBookmarked = false
        binding.favourite.setImageResource(R.drawable.baseline_favorite_border_24)

        CoroutineScope(Dispatchers.IO).launch {
            githubDao.deleteGitHubUserByUsername(username)
            Log.d("FavouriteActivity", "Deleted from favourites: $username")
        }
    }

    private fun checkIsBookmarked(username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = githubDao.getGitHubUserByUsername(username)
            isBookmarked = user != null
            runOnUiThread {
                if (isBookmarked) {
                    binding.favourite.setImageResource(R.drawable.baseline_favorite_24)
                } else {
                    binding.favourite.setImageResource(R.drawable.baseline_favorite_border_24)
                }
            }
        }
    }
}