package com.android.wiragawaskita.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.wiragawaskita.databinding.FragmentHomeBinding
import com.android.wiragawaskita.WorkoutApplication
import androidx.fragment.app.viewModels
import com.android.wiragawaskita.NotificationUtil
import com.android.wiragawaskita.ui.addworkout.AddWorkoutFragment
import com.android.wiragawaskita.R
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import com.android.wiragawaskita.model.Workout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    var firstWorkout: Workout? = null
    var secondWorkout: Workout? = null
    var activeWorkout: Workout? = null

    var firstWorkoutButton: AppCompatButton? = null
    var secondWorkoutButton: AppCompatButton? = null
    var activeWorkoutButton: AppCompatButton? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((requireActivity().application as WorkoutApplication).repository)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val newWorkoutButton: AppCompatButton = binding.btnNewWorkout
        newWorkoutButton.setOnClickListener {
            Log.d("HomeFragment", "Launching Add Workout Fragment.")
            findNavController().navigate(R.id.navigation_addworkout)
        }

        val newWorkoutButton2: AppCompatButton = binding.btnNewWorkout2
        newWorkoutButton2.setOnClickListener {
            Log.d("HomeFragment", "Launching Add Workout Fragment from btnNewWorkout2.")
            findNavController().navigate(R.id.navigation_addworkout)
        }

        val newWorkoutButton3: AppCompatButton = binding.btnNewWorkout3
        newWorkoutButton3.setOnClickListener {
            Log.d("HomeFragment", "Launching Add Workout Fragment from btnNewWorkout3.")
            findNavController().navigate(R.id.navigation_addworkout)
        }

        val newWorkoutButton4: AppCompatButton = binding.btnNewWorkout4
        newWorkoutButton4.setOnClickListener {
            Log.d("HomeFragment", "Launching Add Workout Fragment from btnNewWorkout4.")
            findNavController().navigate(R.id.navigation_addworkout)
        }

        // Schedule the notifications for each workout.
        homeViewModel.allWorkouts.observe(viewLifecycleOwner) { workouts ->
            workouts.let {
                if (it.size > 0) {
                    it[0].id?.let { it1 ->
                        NotificationUtil().createClickableNotification(
                            requireContext(),
                            it[0].name,
                            it[0].category,
                            // This may or may not work "requireContext()".
                            Intent(requireContext(), AddWorkoutFragment::class.java),
                            it1
                        )
                    }
                }
                Log.d("HomeFragment", "Adding Workout: ${workouts.size}")
                if (it.size >= 3) {
                    activeWorkout = it[0]
                    firstWorkout = it[1]
                    secondWorkout = it[2]
                } else if (it.size == 2) {
                    activeWorkout = it[0]
                    firstWorkout = it[1]
                } else if (it.size == 1) {
                    activeWorkout = it[0]
                }
                setWorkouts()
            }
        }

        return root
    }

    @SuppressLint("SetTextI18n")
    fun setWorkouts() {

        if (activeWorkout != null) {
            val date = Date(activeWorkout!!.date)
            activeWorkoutButton?.text = "${activeWorkout!!.name}      ${formatDate(date)}"
            if (System.currentTimeMillis()-3600000 <= activeWorkout!!.date && activeWorkout!!.date <= System.currentTimeMillis()+3600000) {
                activeWorkoutButton?.setBackgroundColor(Color.parseColor("#8E0000"))
            }
            activeWorkoutButton?.setOnClickListener {
                Log.d("HomeFragment", "Active Workout Button Clicked!")
                val workoutId = Bundle()
                activeWorkout!!.id?.let { it1 -> workoutId.putInt("ID", it1)}
                findNavController().navigate(R.id.navigation_activeworkout, workoutId)
            }
        } else {
            activeWorkoutButton?.text = "Tidak ada jadwal workout."
        }

        if (firstWorkout != null) {
            Log.d("HomeFragment", "Adding workout with id: ${firstWorkout!!.id}")
            val date = Date(firstWorkout!!.date)
            firstWorkoutButton?.text = "${firstWorkout!!.name}      ${formatDate(date)}"
            firstWorkoutButton?.setOnClickListener {
                Log.d("HomeFragment", "First Workout Button Clicked!")
                val workoutId = Bundle()
                firstWorkout!!.id?.let { it1 -> workoutId.putInt("ID", it1) }
                findNavController().navigate(R.id.navigation_addworkout, workoutId)
            }
        } else {
            firstWorkoutButton?.text = "Tidak ada jadwal workout."
        }

        if (secondWorkout != null) {
            val date = Date(secondWorkout!!.date)
            secondWorkoutButton?.text = "${secondWorkout!!.name}      ${formatDate(date)}"
            secondWorkoutButton?.setOnClickListener {
                Log.d("HomeFragment", "Second Workout Button Clicked!")
                val workoutId = Bundle()
                secondWorkout!!.id?.let { it1 -> workoutId.putInt("ID", it1) }
                findNavController().navigate(R.id.navigation_addworkout, workoutId)
            }
        } else {
            secondWorkoutButton?.text = "Tidak ada jadwal workout."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun formatDate(date: Date): String {
        val formattedDate = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        return formattedDate.format(date)
    }
}
