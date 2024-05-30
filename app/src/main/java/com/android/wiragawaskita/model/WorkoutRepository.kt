package com.android.wiragawaskita.model

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val workoutDao: WorkoutDao) {
    // Get all of the workouts from the Dao
    val allWorkouts: Flow<List<Workout>> = workoutDao.getWorkoutsByDate()

    // Function to get a live workout from passed in ID (using the Dao)
    fun getWorkout(id:Int):Flow<Workout> {
        return workoutDao.getWorkout(id)
    }

    // Function to get a non-live workout from passed in ID
    fun getWorkoutNotLive(id:Int):Workout {
        return workoutDao.getWorkoutNotLive(id)
    }

    //  Function to insert a workout into the workout database by going through the Dao
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(workout: Workout) {
        workoutDao.insert(workout)
    }

    // Function to update a workout in the workout database
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(workout: Workout) {
        workoutDao.update(workout)
    }

    // Function to delete a workout in the workout database
    // NOTE: This function takes in an ID instead of Workout object to delete an
    // existing workout.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteWorkout(id: Int) {
        workoutDao.deleteWorkout(id)
    }
}