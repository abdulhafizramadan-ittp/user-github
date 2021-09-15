package com.ahr.usergithub.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ahr.usergithub.R
import com.ahr.usergithub.api.GithubUserApi
import com.ahr.usergithub.databinding.FragmentSplashScreenBinding
import com.ahr.usergithub.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            Navigation.findNavController(binding.root).navigate(R.id.action_splashScreenFragment_to_homeFragment)
        }, 2000)
//        getUser()
    }

    private fun getUser() {
        GithubUserApi.instance.getUser().enqueue(object : Callback<ArrayList<User>?> {
            override fun onResponse(call: Call<ArrayList<User>?>, response: Response<ArrayList<User>?>) {
                Log.d("SplashScreenFragment", "onResponse: ${response.body()}")
//                Navigation.findNavController(binding.root).navigate(R.id.action_splashScreenFragment_to_homeFragment)
            }

            override fun onFailure(call: Call<ArrayList<User>?>, t: Throwable) {
                Log.d("SplashScreenFragment", "onFailure: error : ${t.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}