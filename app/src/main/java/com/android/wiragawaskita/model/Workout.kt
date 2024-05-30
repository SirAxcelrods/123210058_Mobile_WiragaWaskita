package com.android.wiragawaskita.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_table")
class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "complete") var complete: Boolean,
    @ColumnInfo(name = "date") var date: Long,
    @ColumnInfo(name = "duration") var duration: Long,
    @ColumnInfo(name = "sets") var sets: Int,
    @ColumnInfo(name = "weight") var weight: Int
)
