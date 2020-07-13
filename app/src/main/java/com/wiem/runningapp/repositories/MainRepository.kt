package com.wiem.runningapp.repositories

import com.wiem.runningapp.db.Run
import com.wiem.runningapp.db.RunDAO
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDAO: RunDAO
) {
    suspend fun insertRun(run : Run) = runDAO.insertRun(run)

    suspend fun deleteRun(run : Run) = runDAO.deleteRun(run)

    fun getAllRunsSorted(column : String) = runDAO.filterBy(column)

    fun getTotalAVGSpeed() = runDAO.getTotalAvgSpeed()

    fun getDistance() = runDAO.getTotalDistance()

    fun getTotalCaloriesBurned() = runDAO.getTotalCaloriesBurned()

    fun getTotalTimeMillis() = runDAO.getTotalTimeMillis()



}