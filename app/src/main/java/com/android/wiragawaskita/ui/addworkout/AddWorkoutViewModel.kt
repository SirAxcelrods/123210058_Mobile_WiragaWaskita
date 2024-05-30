package com.android.wiragawaskita.ui.addworkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.wiragawaskita.model.Workout
import com.android.wiragawaskita.model.WorkoutRepository
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.coroutineScope

class AddWorkoutViewModel(private val repository: WorkoutRepository, private val id:Int = -1) : ViewModel() {

    // Get a list of all the workouts from the repository
    val allWorkouts: LiveData<List<Workout>> = repository.allWorkouts.asLiveData()

    var curWorkout: LiveData<Workout> = repository.getWorkout(id).asLiveData()

    fun updateId(id:Int) {
        curWorkout = repository.getWorkout(id).asLiveData()
    }

    suspend fun insert(workout: Workout) {
        coroutineScope {
            repository.insert(workout)
        }
    }

    suspend fun update(workout: Workout) {
        coroutineScope {
            repository.update(workout)
        }
    }

    suspend fun deleteWorkout(id: Int) {
        coroutineScope {
            repository.deleteWorkout(id)
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is Add Workout Fragment"
    }
    val text: LiveData<String> = _text
}

class AddWorkoutViewModelFactory(private val repository: WorkoutRepository, private val id: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(AddWorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddWorkoutViewModel(repository, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class (AddWorkout)")
    }
}