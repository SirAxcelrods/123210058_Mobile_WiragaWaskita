package com.android.wiragawaskita.ui.addworkout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.wiragawaskita.WorkoutApplication
import com.android.wiragawaskita.databinding.FragmentAddworkoutBinding
import androidx.fragment.app.viewModels
import com.android.wiragawaskita.DatePickerFragment
import com.android.wiragawaskita.TimePickerFragment
import com.android.wiragawaskita.model.Workout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.android.wiragawaskita.R

const val EXTRA_ID: String = "edu.uark.workoutreminderapp.AddWorkoutFragment.EXTRA_ID"
class AddWorkoutFragment : Fragment() {
    private var _binding: FragmentAddworkoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val addWorkoutViewModel: AddWorkoutViewModel by viewModels {
        AddWorkoutViewModelFactory((requireActivity().application as WorkoutApplication).repository, -1)
    }

    private lateinit var editTitle: EditText
    private lateinit var editDesc: EditText
    private lateinit var editCategory: EditText
    private lateinit var editTime: EditText
    private lateinit var editWeight: EditText
    private lateinit var editDate: Button
    private lateinit var editComplete: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val addWorkoutViewModel =
//            ViewModelProvider(this).get(AddWorkoutViewModel::class.java)

        _binding = FragmentAddworkoutBinding.inflate(inflater, container, false)
        val root: View = binding.root
        editTitle = binding.editTitle
        editDesc = binding.editDescription
        editCategory = binding.editCategory
        editTime = binding.editTime
        editWeight = binding.editWeight
        editDate = binding.editTextDate
        editComplete = binding.workoutCheckbox

        val args = arguments
        var id = args?.getInt("ID")
        if (id == null) {
            id = -1
        }

        if (id != -1) {
            addWorkoutViewModel.updateId(id)
        }

        addWorkoutViewModel.curWorkout.observe(viewLifecycleOwner) {
            workout->workout?.let {
                updateViewUI(workout)
            }
        }

        binding.buttonSave.setOnClickListener {
            CoroutineScope(SupervisorJob()).launch {
                if (id==-1) {
                    getWorkoutFromUI()?.let { workout -> addWorkoutViewModel.insert(workout)}
                }
            }
        }

        editDate.setOnClickListener {
            dateClicked()
        }

        val btnSave = binding.buttonSave
        btnSave.setOnClickListener {
            CoroutineScope(SupervisorJob()).launch {
                if (id==-1) {
                    getWorkoutFromUI()?.let { workout -> addWorkoutViewModel.insert(workout) }
                } else {
                    getWorkoutFromUI()?.let { workout -> addWorkoutViewModel.update(workout) }
                }
            }
            findNavController().navigate(R.id.navigation_home)
        }

        val btnDelete = binding.buttonDelete
        btnDelete.setOnClickListener {
            if (id == -1) {
                findNavController().navigate(R.id.navigation_home)
            } else {
                CoroutineScope(SupervisorJob()).launch {
                    addWorkoutViewModel.deleteWorkout(id)
                }
            }
            findNavController().navigate(R.id.navigation_home)
        }

        return root
    }

    fun getWorkoutFromUI(): Workout? {
        val id = -1 // TODO: get workout id passed in here.
        var workout: Workout?
        if (id != -1) {
            workout = addWorkoutViewModel.curWorkout.value
        } else {
            workout = Workout(null, "", "", "", false, 0, 0, 0, 0)
        }
        if (workout != null) {
            workout.name = editTitle.text.toString()
            workout.description = editDesc.text.toString()
            workout.category = editCategory.text.toString()
            workout.duration = editTime.text.toString().toLong()
            workout.weight = editWeight.text.toString().toInt()
            workout.date = java.text.DateFormat.getDateTimeInstance(
                DateFormat.DEFAULT,
                DateFormat.SHORT
            ).parse(editDate.text.toString())?.time!!
            workout.complete = editComplete.isChecked
        }
        return workout
    }

    fun updateViewUI(workout: Workout) {
        editTitle.setText(workout.name)
        editDesc.setText(workout.description)
        editCategory.setText(workout.category)
        editTime.setText(workout.duration.toString())
        editWeight.setText(workout.weight.toString())
        if (workout.date != null) {
            val cal: Calendar = Calendar.getInstance()
            cal.timeInMillis = workout.date!!
            editDate.setText(java.text.DateFormat.getDateTimeInstance(
                DateFormat.DEFAULT,
                DateFormat.SHORT
            ).format(cal.timeInMillis))
        } else {
            editDate.setText("")
        }
        editComplete.isChecked = workout.complete
    }

    fun dateSet(calendar: Calendar) {
        TimePickerFragment(calendar, this::timeSet).show(childFragmentManager, "timePicker")
    }

    fun timeSet(calendar: Calendar) {
        editDate.setText(java.text.DateFormat.getDateTimeInstance(
            DateFormat.DEFAULT,
            DateFormat.SHORT
        ).format(calendar.timeInMillis))
    }

    fun dateClicked() {
        DatePickerFragment(this::dateSet).show(childFragmentManager, "datePicker")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("AddWorkoutFragment", "onDestroyView")
        _binding = null
    }
}