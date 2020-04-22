package com.luca.innocenti.sitesurveynotebook

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(){
    var posizione: Int =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                sposta()
            }
        })
    }

    private fun sposta(){
        // crea directory
        Log.d("Directory","Sposta")
        val sdf = SimpleDateFormat("dd_MM_yyyy_hh:mm:ss")
        val currentDate = sdf.format(Date())
        val folder_main = currentDate

        val f =
            File(this.getExternalFilesDir("SiteSurvey"), folder_main)
        if (!f.exists()) {
            f.mkdirs()
        }
        var mp3_tmp:File = File(this.getExternalFilesDir("SiteSurvey"), "tmp_recording.mp3")
        var jpg_tmp:File= File(this.getExternalFilesDir("SiteSurvey"), "tmp_photo.jpg")

        var mp3_def: File = File(this.getExternalFilesDir("SiteSurvey/"+currentDate), currentDate+".mp3")
        var jpg_def: File = File(this.getExternalFilesDir("SiteSurvey/"+currentDate), currentDate+".jpg")

        mp3_tmp.copyTo(mp3_def,false)
        jpg_tmp.copyTo(jpg_def,false)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.note -> {
            if (posizione == 2) NavHostFragment.findNavController(nav_host_fragment).navigate(R.id.action_SecondFragment_to_FirstFragment)
            posizione = 1
            Log.d("Menu", "Note")
            true
        }
        R.id.list -> {
            Log.d("Menu", "Lista")
            if (posizione == 1) NavHostFragment.findNavController(nav_host_fragment).navigate(R.id.action_FirstFragment_to_SecondFragment)
            posizione = 2
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

}
