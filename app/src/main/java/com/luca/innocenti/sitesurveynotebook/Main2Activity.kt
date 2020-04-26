package com.luca.innocenti.sitesurveynotebook

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.reflect.Method


class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permesso","NON NON ")
        }
        else
        {
            Log.d("Permesso","Accettato")
        }


        val intent = intent
        var name = intent.getStringExtra("s")
        var name_1 = File(getExternalFilesDir("SiteSurvey/" + name), name + ".jpg").toString()

        Log.d("S",name_1)
        val immagine: ImageView = findViewById(R.id.imageView2)
        val pulsante: ImageButton= findViewById(R.id.imageButton)
        val sh: ImageButton = findViewById(R.id.imageshare)

        val destination = File(name_1)
        val fileInputStream: FileInputStream
        fileInputStream = FileInputStream(destination)
        val img = BitmapFactory.decodeStream(fileInputStream)
        immagine.setImageBitmap(img)

        sh.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.type = "text/plain"
                var stringPath_mp3 =File(getExternalFilesDir("SiteSurvey/"+name), name+".mp3")
                var stringPath_jpg=File(getExternalFilesDir("SiteSurvey/"+name), name+".jpg")
                var stringPath_jpg2=File(getExternalFilesDir("SiteSurvey/"+name), name+".jpg").toString()
                var stringPath_mp32=File(getExternalFilesDir("SiteSurvey/"+name), name+".mp3").toString()


                val uris = ArrayList<Uri>()
                uris.add(Uri.parse("file://"+stringPath_jpg2))
                uris.add(Uri.parse("file://"+stringPath_mp32))

                //uris.add(Uri.fromFile(stringPath_jpg))
                //uris.add(Uri.fromFile(stringPath_mp3))

                stringPath_jpg2 = "file://"+ stringPath_jpg2
                stringPath_mp32 = "file://"+ stringPath_mp32

                Log.d("test",stringPath_jpg2)

                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                intent.putExtra(Intent.EXTRA_SUBJECT, "Site Survey "+name)
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(stringPath_jpg2))
                //intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(stringPath_mp32))
                //intent.putExtra(Intent.EXTRA_STREAM, uris)
                //intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

                intent.putExtra(Intent.EXTRA_TEXT, "Site Survey Notebook")
                intent.data = Uri.parse("mailto:")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this.startActivity(intent)
                this.finish()
            } catch (e: Exception) {
                println("is exception raises during sending mail$e")
            }
        }

        pulsante.setOnClickListener {
            var stringPath =File(getExternalFilesDir("SiteSurvey/"+name), name+".mp3").toString()
            stringPath = android.net.Uri.parse("file://" + stringPath).getPath().toString();

            Log.d("mp3",stringPath)
            val file = File(stringPath)
            val inputStream = FileInputStream(file)



            try {
                val MediaPlayer:MediaPlayer? = MediaPlayer().apply {
                    stop()
                    reset()
                    setAudioStreamType(AudioManager.STREAM_MUSIC)
                    setDataSource(inputStream.fd)

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


