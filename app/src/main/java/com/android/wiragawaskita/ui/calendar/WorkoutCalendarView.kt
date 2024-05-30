package com.android.wiragawaskita.ui.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.widget.CalendarView
import java.util.*

// This is a custom calendar view that extends the current calendar view, but allows me to override the onDraw() function
// so that I can highlight the colors on the calendar of workouts.
class WorkoutCalendarView : CalendarView {
    private val highlightDays = HashMap<Long, Int>()
    private val paint = Paint()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        paint.style = Paint.Style.FILL
        this.setWillNotDraw(false)
    }

    // Add a workout with specified color
    fun addHighlightedDay(timeInMillis: Long, color: Int) {
        highlightDays[timeInMillis] = color
        Log.d("WorkoutCalendarView", "Invalidate Called.")
    }

    // Remove workout from calendar view
    fun removeHighlightedDay(timeInMillis: Long) {
        highlightDays.remove(timeInMillis)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        Log.d("WorkoutCalendarView", "onDraw() called.")
        super.onDraw(canvas)

        val firstVisibleDayOfMonth = getFirstVisibleDay()
        val cellWidth = width / 10f
        val cellHeight = height / 10f
        val cellMargin = 2f

        for ((dayInMillis, color) in highlightDays) {
            val day = dayOfMonthFromTimeInMillis(dayInMillis)
            val cell = getDayCell(day, firstVisibleDayOfMonth, cellWidth, cellHeight, cellMargin)
            drawHighlightedDay(canvas, cell, color)
        }
    }

    private fun drawHighlightedDay(canvas: Canvas, cell: FloatArray, color: Int) {
        Log.d("WorkoutCalendarView", "Calling drawHighlightedDay")
        paint.color = color
        canvas.drawCircle(cell[0], cell[1], cell[2] / 2, paint)
    }

    private fun getDayCell(day: Int, firstVisibleDayOfMonth: Int, cellWidth: Float, cellHeight: Float, cellMargin: Float): FloatArray {
        val dayOfMonth = day - firstVisibleDayOfMonth
        val col = dayOfMonth % 7
        val row = dayOfMonth / 7
        val x = col * (cellWidth + cellMargin) + 16.0F
        val y = row * (cellHeight + cellMargin) + 374.0F
        Log.d("WorkoutCalendarView", "DayofMonth: ${dayOfMonth} day: $day firstVisibleDayOfMonth: $firstVisibleDayOfMonth")
        return floatArrayOf(x + cellWidth / 2, y + cellHeight / 2, 100.0F)
    }

    private fun getFirstVisibleDay(): Int {
        val calendar = Calendar.getInstance()
        val firstDayOfWeek = firstDayOfWeek
        val curDayWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val daysUntilVisDay = (curDayWeek - firstDayOfWeek + 7) % 7
        calendar.add(Calendar.DAY_OF_MONTH, -daysUntilVisDay)
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun dayOfMonthFromTimeInMillis(timeInMillis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

}