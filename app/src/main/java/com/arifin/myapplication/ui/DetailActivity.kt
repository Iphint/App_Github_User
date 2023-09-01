package com.arifin.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.arifin.myapplication.R
import com.arifin.myapplication.data.retrofit.ApiConfig
import com.arifin.myapplication.data.response.DetailUserResponse
import com.arifin.myapplication.databinding.ActivityDetailBinding
import com.arifin.myapplication.ui.fragment.SectionsPagerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username") ?: ""

        supportActionBar?.hide()

        val apiService = ApiConfig.getApiServices()
        val call = apiService.getDetailUser(username)

        call.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {

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
}