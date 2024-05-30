package com.android.wiragawaskita.ui.activeworkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.wiragawaskita.R
import com.android.wiragawaskita.WorkoutApplication
import com.android.wiragawaskita.databinding.FragmentActiveworkoutBinding
import com.android.wiragawaskita.model.Workout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch



class ActiveWorkoutFragment : Fragment() {
    private var _binding: FragmentActiveworkoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var editSets: EditText
    private lateinit var editTitle: TextView
    private lateinit var editCategory: TextView
    private lateinit var editTime: EditText
    private lateinit var editWeight: EditText

    private val activeWorkoutViewModel: ActiveWorkoutViewModel by viewModels {
        ActiveWorkoutViewModelFactory((requireActivity().application as WorkoutApplication).repository, -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val activeWorkoutViewModel =
//            ViewModelProvider(this).get(ActiveWorkoutViewModel::class.java)

        _binding = FragmentActiveworkoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val args = arguments
        var id = args?.getInt("ID")
        if (id == null) {
            id = -1
        }

        if (id != -1) {
            activeWorkoutViewModel.updateId(id)
        }

        editSets = binding.activeSets
        editTitle = binding.activeTitle
        editCategory = binding.activeCategory
        editTime = binding.activeTime
        editWeight = binding.activeWeight

        val btnSave = binding.btnSave
        btnSave.setOnClickListener {
            CoroutineScope(SupervisorJob()).launch {
                getWorkoutFromUI()?.let { workout -> activeWorkoutViewModel.update(workout) }
            }
            findNavController().navigate(R.id.navigation_home)
        }


        activeWorkoutViewModel.curWorkout.observe(viewLifecycleOwner) {
            workout->workout?.let {
                updateViewUI(workout)
            }
        }
        return root
    }

    private fun getWorkoutFromUI(): Workout? {
        var workout: Workout?
        workout = activeWorkoutViewModel.curWorkout.value
        if (workout != null) {
            workout.sets = editSets.text.toString().toInt()
            workout.duration = editTime.text.toString().toLong()
            workout.weight = editWeight.text.toString().toInt()
        }
        return workout
    }


    fun updateViewUI(workout: Workout) {
        editTitle.setText(workout.name)
        editCategory.setText(workout.category)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}