package com.example.workmanagerexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.workmanagerexample.UploadWorker.Companion.WORKER_KEY

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            setOneTimeWorkRequest()
        }
    }

    private fun setOneTimeWorkRequest() {
        val workManager = WorkManager.getInstance(applicationContext)
        val constriant = Constraints.Builder().setRequiresCharging(true).setRequiredNetworkType(NetworkType.CONNECTED).build()


        val uploadRequest =
            OneTimeWorkRequest.Builder(UploadWorker::class.java).setConstraints(constriant).build()

        workManager.enqueue(uploadRequest)
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
}