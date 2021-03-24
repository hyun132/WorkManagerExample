package com.example.workmanagerexample

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DownLoadingWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        try {

            for (i in 0..1000) {
                Log.i("MYTAG", "DownLoading $i")
            }
            val time = SimpleDateFormat("dd/M/yy hh:mm:ss")
            val currentData = time.format(Date())

            Log.i("MYTAG", "Complete $currentData")
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}