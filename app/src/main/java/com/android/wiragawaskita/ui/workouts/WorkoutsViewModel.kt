package com.android.wiragawaskita.ui.workouts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.wiragawaskita.model.Workout
import com.android.wiragawaskita.model.WorkoutRepository
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData

class WorkoutsViewModel(private val repository: WorkoutRepository) : ViewModel() {

    // Get a list of all the workouts from the repository.
    val allWorkouts: LiveData<List<Workout>> = repository.allWorkouts.asLiveData()

    private val _text = MutableLiveData<String>().apply {
        value = "This is workouts Fragment"
    }
    val text: LiveData<String> = _text
}

class WorkoutsViewModelFactory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(WorkoutsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkoutsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class (Workouts)")
    }
}