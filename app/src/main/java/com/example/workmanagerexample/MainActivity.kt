package com.example.workmanagerexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.workmanagerexample.UploadWorker.Companion.WORKER_KEY
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
//            setOneTimeWorkRequest()
            setPeriodicWorkRequest()
        }
    }

    private fun setOneTimeWorkRequest() {
        val workManager = WorkManager.getInstance(applicationContext)
        val constriant = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()


        val uploadRequest =
            OneTimeWorkRequest.Builder(UploadWorker::class.java).setConstraints(constriant).build()

        val filteringRequest = OneTimeWorkRequest.Builder(FilteringWorker::class.java).build()

        val compressRequest = OneTimeWorkRequest.Builder(CompressingWorker::class.java).build()
        val downloadRequest = OneTimeWorkRequest.Builder(DownLoadingWorker::class.java).build()

        val paralleWorks=  mutableListOf<OneTimeWorkRequest>()
        paralleWorks.add(filteringRequest)
        paralleWorks.add(downloadRequest)

        workManager.beginWith(paralleWorks)
            .then(compressRequest)
            .then(uploadRequest)
            .enqueue()

//        workManager.enqueue(uploadRequest)
        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this, Observer{
                findViewById<TextView>(R.id.textView).text = it.state.name
                if (it.state.isFinished){
                    val data = it.outputData
                    val message = data.getString(WORKER_KEY.toString())
                    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setPeriodicWorkRequest(){
        val periodicWorkRequest = PeriodicWorkRequest.Builder(DownLoadingWorker::class.java,20,TimeUnit.MINUTES).build()
        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)
    }
}