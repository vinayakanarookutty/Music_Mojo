package com.example.musicmojo
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

import android.os.Build

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import android.app.Activity
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MyAdapter(val context: Activity, val dataList: List<Data>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    companion object {
        const val CHANNEL_ID = "MusicPlayerChannel"
        const val NOTIFICATION_ID = 101

    }
//    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.music_card, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentData = dataList[position]
        holder.title.text = currentData.title

        Picasso.get().load(currentData.album.cover).into(holder.image)

//        // Release previous MediaPlayer instance
//        mediaPlayer?.release()

        // Initialize MediaPlayer
       val mediaPlayer = MediaPlayer.create(context, currentData.preview.toUri())

        holder.play.setOnClickListener {
            showNotification(holder.title.text as String)
            mediaPlayer?.start()
        }
        holder.pause.setOnClickListener {
            mediaPlayer?.stop()
            cancelNotification()
        }
    }
    private fun showNotification(songName: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Now Playing")
            .setContentText(songName)
            .setSmallIcon(R.drawable.designer) // Set your app's icon here
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Check for permission to post notifications
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Handle the case where permission is not granted
            // You may request the missing permissions here
            // or handle the absence of permission as needed
            return
        }

        // Notify using NotificationManagerCompat
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notification)
        }
    }


    private fun cancelNotification() {
        val notificationManager =
            ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.musicImageView)
        val title: TextView = itemView.findViewById(R.id.musicTitle)
        val play: ImageButton = itemView.findViewById(R.id.btnPlay)
        val pause: ImageButton = itemView.findViewById(R.id.btnPause)
    }
}
