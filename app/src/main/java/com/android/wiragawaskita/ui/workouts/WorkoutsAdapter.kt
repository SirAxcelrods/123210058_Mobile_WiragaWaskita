package com.android.wiragawaskita.ui.workouts

import android.graphics.Color
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.wiragawaskita.model.Workout
import com.android.wiragawaskita.R
import android.widget.TextView
import java.util.Calendar
import android.icu.text.DateFormat
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil


class WorkoutsAdapter(val itemClicked:(workout:Workout)->Unit) : ListAdapter<Workout, WorkoutsAdapter.WorkoutViewHolder>(WorkoutTaskComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
//        val current = getItem(position)
//        current.id?.let {
//            // Change this later if we want to change what the user sees in the workouts list page.
//            holder.bind(it, current.name, current.category, current.complete, current.date)
//        }
//        holder.itemView.tag = current.id
//        holder.itemView.setOnClickListener {
//            val itemId = it.tag
//            itemClicked(it.tag as Int)
//        }
        val current = getItem(position)
        holder.bind(current.name, current.category, current.complete, current.date, current.description)
        holder.itemView.tag = current
        holder.itemView.setOnClickListener {
            itemClicked(holder.itemView.tag as Workout)
        }
    }

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val workoutNameView: TextView = itemView.findViewById(R.id.tvName)
        private val workoutCategoryView: TextView = itemView.findViewById(R.id.tvCategory)
        private val workoutDateView: TextView = itemView.findViewById(R.id.tvDate)
        private val workoutDescriptionView: TextView = itemView.findViewById(R.id.tvDescription)

        fun bind(name: String?, category: String?, completed: Boolean, date: Long?, desc: String?) {
            workoutNameView.text = name
            workoutCategoryView.text = category
            workoutDescriptionView.text = desc
            if (date != null) {
                val cal: Calendar = Calendar.getInstance()
                cal.timeInMillis = date
                workoutDateView.setText(
                    java.text.DateFormat.getDateTimeInstance(
                        DateFormat.DEFAULT,
                        DateFormat.SHORT
                    ).format(cal.timeInMillis)
                )
//                if (completed) {
//                    workoutNameView.setBackgroundColor(Color.parseColor("#73c977"))
//                    workoutCategoryView.setBackgroundColor(Color.parseColor("#73c977"))
//                    workoutDateView.setBackgroundColor(Color.parseColor("#73c977"))
//                } else if (!completed && cal.timeInMillis > System.currentTimeMillis() ) {
//                    workoutNameView.setBackgroundColor(Color.parseColor("#EEEF8B"))
//                    workoutCategoryView.setBackgroundColor(Color.parseColor("#EEEF8B"))
//                    workoutDateView.setBackgroundColor(Color.parseColor("#EEEF8B"))
//                } else {
//                    workoutNameView.setBackgroundColor(Color.parseColor("#c94d4d"))
//                    workoutCategoryView.setBackgroundColor(Color.parseColor("#c94d4d"))
//                    workoutDateView.setBackgroundColor(Color.parseColor("#c94d4d"))
//                }
            }
        }

        companion object {
            fun create(parent: ViewGroup) : WorkoutViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_workout, parent, false)
                return WorkoutViewHolder(view)
            }
        }
    }

    class WorkoutTaskComparator : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
           return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.name == newItem.name
        }
    }
}