package com.android.wiragawaskita.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.wiragawaskita.model.Workout
import com.android.wiragawaskita.model.WorkoutRepository
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModelProvider

class CalendarViewModel(private val repository: WorkoutRepository) : ViewModel() {

    // Get a list of all the workouts from the repository.
    val allWorkouts: LiveData<List<Workout>> = repository.allWorkouts.asLiveData()

    private val _text = MutableLiveData<String>().apply {
        value = "This is calendar Fragment"
    }
    val text: LiveData<String> = _text
}

class CalendarViewModelFactory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalendarViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class (Calendar)")
    }
}
