package com.android.wiragawaskita


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import android.widget.ProgressBar




class SplashFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE // Tampilkan ProgressBar saat splash screen ditampilkan

        Handler().postDelayed({
            val navController = findNavController(view)
            navController.navigate(R.id.navigation_login)
        }, 5000) // Menunggu selama 3 detik sebelum pindah ke HomeFragment
    }
}

