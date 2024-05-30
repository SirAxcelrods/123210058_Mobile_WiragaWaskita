package com.android.wiragawaskita

import android.app.Application
import com.android.wiragawaskita.model.WorkoutRepository
import com.android.wiragawaskita.model.WorkoutDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WorkoutApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { WorkoutDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { WorkoutRepository(database.workoutDao()) }
}