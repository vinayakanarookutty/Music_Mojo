package com.example.musicmojo
import android.content.Context
import android.content.Intent
import android.net.Uri


import androidx.cardview.widget.CardView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VideoAdapter(private val context: Context,private val videoList: List<VideoData>) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videoList[position]
        holder.videoName.text = video.name
        holder.cardView.setOnClickListener {
            playVideo(video.path)
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val videoName: TextView = itemView.findViewById(R.id.textViewVideoName)
    }
    private fun playVideo(videoPath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(videoPath), "video/*")
        context.startActivity(intent)
    }
}
