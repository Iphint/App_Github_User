package com.arifin.myapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arifin.myapplication.R
import com.arifin.myapplication.data.response.ResponseFollowingItem
import com.arifin.myapplication.data.retrofit.ApiConfig
import com.arifin.myapplication.ui.FollowingAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {

    private lateinit var followingAdapter: FollowingAdapter
    private lateinit var recyclerViewFollowing: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var textView: TextView

    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowingFragment {
            val fragment = FollowingFragment()
            val args = Bundle()
            args.putString(ARG_USERNAME, username)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_following, container, false)
        recyclerViewFollowing = view.findViewById(R.id.recyclerViewFollowing)
        followingAdapter = FollowingAdapter()
        loadingProgressBar = view.findViewById(R.id.loading)

        recyclerViewFollowing.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewFollowing.adapter = followingAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_USERNAME) ?: ""
        val apiService = ApiConfig.getApiServices()
        val call = apiService.getFollowing(username)

        val textEmpty: TextView = view.findViewById(R.id.textEmpty)

        call.enqueue(object : Callback<List<ResponseFollowingItem>> {
            override fun onResponse(
                call: Call<List<ResponseFollowingItem>>,
                response: Response<List<ResponseFollowingItem>>
            ) {
                loadingProgressBar.visibility = View.GONE
                recyclerViewFollowing.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    val followingList = response.body()
                    if (followingList.isNullOrEmpty()) {
                        // Data kosong, tampilkan notifikasi
                        recyclerViewFollowing.visibility = View.GONE
                        textEmpty.visibility = View.VISIBLE
                    } else {
                        followingAdapter.setData(followingList)
                    }
                } else {
                    // Handle API error
                }
            }

            override fun onFailure(call: Call<List<ResponseFollowingItem>>, t: Throwable) {
                loadingProgressBar.visibility = View.GONE
                // Handle network error
            }
        })
    }
}

