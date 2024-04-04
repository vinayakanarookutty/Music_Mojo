package com.example.musicmojo
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri

import android.provider.MediaStore



import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity

class VideoActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var videoAdapter: VideoAdapter
    private var videoList: MutableList<VideoData> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_xml)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        videoAdapter = VideoAdapter(this,videoList)
        recyclerView.adapter = videoAdapter

        fetchVideoFiles()

    }
    private fun fetchVideoFiles() {
        val contentResolver: ContentResolver = contentResolver
        val videoUri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATA
        )

        val cursor: Cursor? = contentResolver.query(videoUri, projection, null, null, null)

        cursor?.use { cursor ->
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)

            while (cursor.moveToNext()) {
                val name = cursor.getString(nameColumn)
                val path = cursor.getString(pathColumn)
                videoList.add(VideoData(name, path))
            }
        }

        videoAdapter.notifyDataSetChanged()
    }
}
