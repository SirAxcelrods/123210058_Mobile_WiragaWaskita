package com.android.wiragawaskita

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.android.wiragawaskita.databinding.ActivityMainBinding
import com.android.wiragawaskita.model.AppDatabase
import com.android.wiragawaskita.model.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var isLoginOrRegisterProcess = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase

    private var notificationPermissionGranted = false
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                NotificationUtil().createNotificationChannel(this)
            } else {
                // Add code here to explain to user that feature is disabled with notifications disabled.
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cek dan minta izin lokasi
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED) {
            // Izin lokasi sudah diberikan

        } else {
            // Izin lokasi belum diberikan, meminta izin
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        // Cek dan minta izin notifikasi
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED) {
            notificationPermissionGranted = true
            NotificationUtil().createNotificationChannel(this)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-database"
        ).build()

        // Contoh penggunaan database untuk insert user di thread terpisah
        thread {
            val user = User(email = "example@example.com", password = "password")
            db.userDao().insertUser(user)

            // Contoh penggunaan database untuk mendapatkan user berdasarkan email dan password
            val userLoggedIn = db.userDao().getUserByEmailAndPassword("example@example.com", "password")
            runOnUiThread {
                if (userLoggedIn != null) {
                    // Pengguna berhasil login
                } else {
                    // Kombinasi email dan password tidak cocok
                }
            }
        }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.navigation_home) {
                supportActionBar?.show() // menampilkan action bar
                navView.visibility = View.VISIBLE // menampilkan bottom navigation
            } else {
                supportActionBar?.hide() // menyembunyikan action bar
                navView.visibility = View.GONE // menyembunyikan bottom navigation
            }
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_calendar, R.id.navigation_workouts
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
