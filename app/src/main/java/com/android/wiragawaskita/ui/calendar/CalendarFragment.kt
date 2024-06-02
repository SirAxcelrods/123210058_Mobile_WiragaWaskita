package com.android.wiragawaskita.ui.calendar

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.wiragawaskita.WorkoutApplication
import com.android.wiragawaskita.databinding.FragmentCalendarBinding
import com.android.wiragawaskita.model.Workout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import java.io.IOException
import java.util.Locale


class CalendarFragment : Fragment() {

    private lateinit var weatherInfoTextView: TextView
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationTextView: TextView

    private val calendarViewModel: CalendarViewModel by viewModels {
        CalendarViewModelFactory((requireActivity().application as WorkoutApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val calendarView: WorkoutCalendarView = binding.calendar
        locationTextView = binding.locationTextView

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermission()

        calendarViewModel.allWorkouts.observe(viewLifecycleOwner) { workouts ->
            workouts?.let {
                drawWorkoutsOnCalendar(it, calendarView)
            }
            calendarView.invalidate()
        }

        return root

    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            getLastLocation()
        }
    }

    private fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener(requireActivity()) { location ->
                location?.let {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    try {
                        val addresses: List<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        addresses?.let { // mengganti addresses dengan it
                            if (it.isNotEmpty()) {
                                val address: Address = it[0]
                                val locationText = "${address.subLocality}, ${address.locality}, ${address.adminArea}"
                                locationTextView.text = locationText
                            } else {
                                locationTextView.text = "Unable to find location"
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        locationTextView.text = "Unable to find location"
                    }
                }
            }
    }


    private fun drawWorkoutsOnCalendar(workoutList: List<Workout>, calendarView: WorkoutCalendarView) {
        for (workout in workoutList) {
            if (workout.complete) {
                calendarView.addHighlightedDay(workout.date, Color.BLUE)
            } else {
                calendarView.addHighlightedDay(workout.date, Color.LTGRAY)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
