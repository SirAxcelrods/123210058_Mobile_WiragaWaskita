package com.android.wiragawaskita.ui.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.wiragawaskita.databinding.FragmentWorkoutsBinding
import androidx.fragment.app.viewModels
import com.android.wiragawaskita.WorkoutApplication
import android.content.Intent
import android.util.Log
import com.android.wiragawaskita.ui.addworkout.AddWorkoutFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.wiragawaskita.R
import com.android.wiragawaskita.ui.addworkout.EXTRA_ID

class WorkoutsFragment : Fragment() {

    private var _binding: FragmentWorkoutsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val workoutsViewModel: WorkoutsViewModel by viewModels {
        WorkoutsViewModelFactory((requireActivity().application as WorkoutApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val workoutsViewModel =
//            ViewModelProvider(this).get(WorkoutsViewModel::class.java)
        _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val newWorkoutButton = binding.btnNewWorkout
        newWorkoutButton.setOnClickListener {
            val workoutId = Bundle()
            workoutId.putInt("ID", -1)
            findNavController().navigate(R.id.navigation_addworkout, workoutId)
        }

        val recyclerView = binding.recyclerview
        val adapter = WorkoutsAdapter {
            it.id?.let { it1 -> listItemClicked(it1) }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // This should go through each workout and create the list item for it using the adapter.
        workoutsViewModel.allWorkouts.observe(viewLifecycleOwner) { workouts ->
            Log.d("WorkoutsFragment", "Adding workout: ${workouts.size}")
            workouts.let {
                adapter.submitList(it)
            }
        }

        return root
    }

    private fun listItemClicked(id: Int) {
        val workoutId = Bundle()
        workoutId.putInt("ID", id)
        findNavController().navigate(R.id.navigation_addworkout, workoutId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}