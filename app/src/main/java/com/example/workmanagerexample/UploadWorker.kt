package com.example.workmanagerexample

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class UploadWorker(context: Context,params:WorkerParameters): Worker(context,params) {

    companion object{
        const val WORKER_KEY =1
    }

    override fun doWork(): Result {
        try {

            for (i in 0..300) {
                Log.i("MYTAG", "Uploading $i")
            }

            val time = SimpleDateFormat("dd/M/yy hh:mm:ss")
            val currentData = time.format(Date())

            val outPutData = Data.Builder().putString(WORKER_KEY.toString(),currentData).build()

            return Result.success(outPutData)
        }catch (e:Exception){
            return Result.failure()
        }
    }
}