package com.example.musicmojo

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var myRecyclerView: RecyclerView
    lateinit var myAdapter: MyAdapter
    lateinit var artistSearch: EditText
    lateinit var button: Button
    lateinit var videoButtonClick: Button

    private val CHANNEL_ID = "MusicPlayerChannel"
    private val NOTIFICATION_ID = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        myRecyclerView = findViewById(R.id.recyclerView)
        artistSearch = findViewById(R.id.searchEditText)
        videoButtonClick = findViewById(R.id.videoButton)
        button = findViewById(R.id.searchButton)
        val intent = Intent(this, VideoActivity::class.java)

        videoButtonClick.setOnClickListener {
            startActivity(intent)
        }

        button.setOnClickListener {

            val artistName: String = artistSearch.text.toString()

            val retrofitBuilder = Retrofit.Builder()
                .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
            val retrofitData = retrofitBuilder.getData(artistName)
            retrofitData.enqueue(object : Callback<MyData?> {
                override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                    val dataList = response.body()?.data!!
                    myAdapter = MyAdapter(this@MainActivity, dataList)
                    myRecyclerView.adapter = myAdapter
                    myRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    Log.d("TAG:onResponse", "onResponse: " + response.body())
                }

                override fun onFailure(call: Call<MyData?>, t: Throwable) {
                    Log.d("TAG:onFailure", "onFailure: " + t.message)
                }
            })

        }

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
        val retrofitData = retrofitBuilder.getData("mgsreekumar")
        retrofitData.enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                val dataList = response.body()?.data!!
                myAdapter = MyAdapter(this@MainActivity, dataList)
                myRecyclerView.adapter = myAdapter
                myRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                createNotificationChannel()
                Log.d("TAG:onResponse", "onResponse: " + response.body())
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Log.d("TAG:onFailure", "onFailure: " + t.message)
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Player Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(songName: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE)
            == PackageManager.PERMISSION_GRANTED) {
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Now Playing")
                .setContentText(songName)
                .setSmallIcon(R.drawable.designer) // Set your app's icon here
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            with(NotificationManagerCompat.from(this)) {
                notify(NOTIFICATION_ID, notification)
            }
        } else {
            // Handle the case where permission is not granted
            // You can request permission here or inform the user why the notification cannot be shown
            Log.d("Permission", "Foreground service permission not granted")
        }
    }
}
