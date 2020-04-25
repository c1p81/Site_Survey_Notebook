package com.luca.innocenti.sitesurveynotebook

import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val intent = intent
        var name = intent.getStringExtra("s")
        var name_1 = File(getExternalFilesDir("SiteSurvey/" + name), name + ".jpg").toString()

        Log.d("S",name_1)
        val immagine: ImageView = findViewById(R.id.imageView2)
        val pulsante: ImageButton= findViewById(R.id.imageButton)

        val destination = File(name_1)
        val fileInputStream: FileInputStream
        fileInputStream = FileInputStream(destination)
        val img = BitmapFactory.decodeStream(fileInputStream)
        immagine.setImageBitmap(img)

        pulsante.setOnClickListener {
            var stringPath =File(getExternalFilesDir("SiteSurvey/"+name), name+".mp3").toString()
            stringPath = android.net.Uri.parse("file://" + stringPath).getPath().toString();

            Log.d("mp3",stringPath)

            try {
                val MediaPlayer:MediaPlayer? = MediaPlayer().apply {
                    stop()
                    reset()
                    setAudioStreamType(AudioManager.STREAM_MUSIC)
                    setDataSource(stringPath)

                    //setDataSource(applicationContext,stringPath)
                    //prepareAsync()
                    prepare()
                    start()
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }


    }
}


