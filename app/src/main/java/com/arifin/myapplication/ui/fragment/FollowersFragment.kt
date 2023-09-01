package com.arifin.myapplication.ui.fragment

import android.os.Bundle
import retrofit2.Call
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arifin.myapplication.R
import com.arifin.myapplication.data.response.ResponseFollowersItem
import com.arifin.myapplication.data.retrofit.ApiConfig
import com.arifin.myapplication.ui.FollowersAdapter
import retrofit2.Response
import retrofit2.Callback

class FollowersFragment : Fragment() {

    private lateinit var followersAdapter: FollowersAdapter
    private lateinit var recyclerViewFollowers: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var textView: TextView

    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowersFragment {
            val fragment = FollowersFragment()
            val args = Bundle()
            args.putString(ARG_USERNAME, username)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_followers, container, false)
        recyclerViewFollowers = view.findViewById(R.id.recyclerViewFollowers)
        followersAdapter = FollowersAdapter()
        loadingProgressBar = view.findViewById(R.id.loadingFollowers)

        recyclerViewFollowers.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewFollowers.adapter = followersAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_USERNAME) ?: ""
        val apiService = ApiConfig.getApiServices()
        val call = apiService.getFollowers(username)

        val textEmpty: TextView = view.findViewById(R.id.textEmptyFollowers)

        call.enqueue(object : Callback<List<ResponseFollowersItem>> {
            override fun onResponse(
                call: Call<List<ResponseFollowersItem>>,
                response: Response<List<ResponseFollowersItem>>
            ) {
                loadingProgressBar.visibility = View.GONE
                recyclerViewFollowers.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    val followingList = response.body()
                    if (followingList.isNullOrEmpty()) {
                        // Data kosong, tampilkan notifikasi
                        recyclerViewFollowers.visibility = View.GONE
                        textEmpty.visibility = View.VISIBLE
                    } else {
                        followersAdapter.setData(followingList)
                    }
                } else {
                    // Handle API error
                }
            }

            override fun onFailure(call: Call<List<ResponseFollowersItem>>, t: Throwable) {
                loadingProgressBar.visibility = View.GONE
                // Handle network error
            }
        })
    }
}

