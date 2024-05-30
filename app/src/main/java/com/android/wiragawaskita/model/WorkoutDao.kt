package com.android.wiragawaskita.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    // Get the workouts sorted alphabetically
    @Query("SELECT * FROM workout_table ORDER BY name ASC")
    fun getAlphabetizedWorkouts(): Flow<List<Workout>>

    // Get a single workout with passed in ID
    @Query("SELECT * FROM workout_table WHERE id=:id")
    fun getWorkout(id:Int): Flow<Workout>

    //Get a single workout with passed in ID (Not live version)
    @Query("SELECT * FROM workout_table WHERE id=:id")
    fun getWorkoutNotLive(id:Int): Workout

    // Insert a single workout
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workout: Workout)

    // Delete all of the workouts from the table (testing purposes)
    @Query("DELETE FROM workout_table")
    suspend fun deleteAllWorkouts()

    // Delete a single workout with passed in ID
    @Query("DELETE FROM workout_table WHERE id=:id")
    suspend fun deleteWorkout(id:Int)

    // Update a single workout
    @Update
    suspend fun update(workout: Workout):Int

    // Get the workouts sorted by date of workout.
    @Query("SELECT * FROM workout_table ORDER BY date ASC")
    fun getWorkoutsByDate(): Flow<List<Workout>>
}