package com.android.wiragawaskita.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.wiragawaskita.model.Workout
import com.android.wiragawaskita.model.WorkoutRepository
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.coroutineScope

class HomeViewModel(private val repository: WorkoutRepository): ViewModel() {

    val allWorkouts: LiveData<List<Workout>> = repository.allWorkouts.asLiveData()

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}

class HomeViewModelFactory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class (Home)")
    }
}