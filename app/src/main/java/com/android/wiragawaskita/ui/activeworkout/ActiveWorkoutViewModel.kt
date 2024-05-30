package com.android.wiragawaskita.ui.activeworkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.android.wiragawaskita.model.Workout
import com.android.wiragawaskita.model.WorkoutRepository
import com.android.wiragawaskita.ui.addworkout.AddWorkoutViewModel
import kotlinx.coroutines.coroutineScope

class ActiveWorkoutViewModel(private val repository: WorkoutRepository, private val id: Int = -1) : ViewModel() {
    val allWorkouts: LiveData<List<Workout>> = repository.allWorkouts.asLiveData()

    var curWorkout: LiveData<Workout> = repository.getWorkout(id).asLiveData()

    fun updateId(id: Int) {
        curWorkout = repository.getWorkout(id).asLiveData()
    }

    suspend fun update(workout: Workout) {
        coroutineScope {
            repository.update(workout)
        }
    }

    suspend fun updateSets(sets: Int) {
        coroutineScope {

        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is Active Workout Fragment"
    }
    val text: LiveData<String> = _text
}

class ActiveWorkoutViewModelFactory(private val repository: WorkoutRepository, private val id: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(ActiveWorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActiveWorkoutViewModel(repository, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class (ActiveWorkout)")
    }
}