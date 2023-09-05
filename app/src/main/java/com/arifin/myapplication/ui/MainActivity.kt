package com.arifin.myapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arifin.myapplication.data.response.GithubResponse
import com.arifin.myapplication.data.response.ItemsItem
import com.arifin.myapplication.data.retrofit.ApiConfig
import com.arifin.myapplication.databinding.ActivityMainBinding
import com.arifin.myapplication.model.SettingPreferences
import com.arifin.myapplication.model.dataStore
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: GithubAdapter
    private lateinit var settingsManager: SettingPreferences
    private val userList = mutableListOf<ItemsItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.favourite.setOnClickListener {
            val intent = Intent(this, FavouriteActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.setting.setOnClickListener {
            val intent = Intent(this, DarkModeActivity::class.java)
            startActivity(intent)
            finish()
        }

        settingsManager = SettingPreferences.getInstance(applicationContext.dataStore)
        lifecycleScope.launch {
            settingsManager.getThemeSetting().collect { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(itemDecoration)

        adapter = GithubAdapter(userList)
        adapter.onItemClick = { user ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("username", user.login)
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter

        binding.searchView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchView.text.toString()
                performSearch(query)
                return@setOnEditorActionListener true
            }
            false
        }

        binding.searchView.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = binding.searchView.text.toString()
                performSearch(query)
                return@setOnKeyListener true
            }
            false
        }

        binding.btnSearch.setOnClickListener {
            val query = binding.searchView.text.toString()
            performSearch(query)
        }

        fetchGithubUsers()
    }

    private fun fetchGithubUsers() {
        showLoading(true)
        val apiService = ApiConfig.getApiServices()

        val call = apiService.searchUsers("arif")

        call.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val githubResponse = response.body()
                    githubResponse?.items?.let {
                        userList.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("MainActivity", "Error: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }

    private fun performSearch(query: String) {
        showLoading(true)
        val apiService = ApiConfig.getApiServices()

        val call = apiService.searchUsers(query)

        call.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val githubResponse = response.body()
                    githubResponse?.items?.let {
                        userList.clear()
                        userList.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("MainActivity", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}


