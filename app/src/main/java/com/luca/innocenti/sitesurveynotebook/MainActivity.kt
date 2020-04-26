package com.luca.innocenti.sitesurveynotebook

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.luca.innocenti.sitesurveynotebook.variabili.Companion.audio
import com.luca.innocenti.sitesurveynotebook.variabili.Companion.photo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.luca.innocenti.sitesurveynotebook.variabili.Companion.posizione


class MainActivity : AppCompatActivity(){
    var posizione: Int =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        audio = false
        photo = false
        posizione = 1

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())


        fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (view != null) {
                    sposta(view)
                }
            }
        })
    }

    private fun sposta(vista:View){
        

        //Log.d("variabili_main", stato.audio.toString()+ " "+stato.photo.toString())
        //if (true)
        if (audio && photo)
        {
            // crea sposta i dati dal tmeporaneo alla directory
            Log.d("Directory","Sposta")
            val sdf = SimpleDateFormat("dd_MM_yyyy_hh:mm:ss")
            val currentDate = sdf.format(Date())
            val folder_main = currentDate

            val f =
                File(this.getExternalFilesDir("SiteSurvey"), folder_main)
            if (!f.exists()) {
                f.mkdirs()
            }
            var mp3_tmp:File = File(this.getExternalFilesDir("SiteSurvey/tmp"), "tmp_recording.mp3")
            var jpg_tmp:File= File(this.getExternalFilesDir("SiteSurvey/tmp"), "tmp_photo.jpg")
            Log.d("file", mp3_tmp.toString())
            Log.d("file", jpg_tmp.toString())

            var mp3_def: File = File(this.getExternalFilesDir("SiteSurvey/"+currentDate), currentDate+".mp3")
            var jpg_def: File = File(this.getExternalFilesDir("SiteSurvey/"+currentDate), currentDate+".jpg")

            Log.d("file", mp3_def.toString())
            Log.d("file", jpg_def.toString())

            mp3_tmp.copyTo(mp3_def,false)
            jpg_tmp.copyTo(jpg_def,false)
            audio = false
            photo = false

        }
        else
        {
            Snackbar.make(vista, "First take a photo and record the audio note", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }



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
        R.id.help -> {
            val dialog = AlertDialog.Builder(this).setTitle("Help").setMessage("To record the audio, press and hold the button. After taking the photo and audio recording, press the + button to confirm.")
                .setPositiveButton("Ok", { dialog, i -> })

            dialog.show()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

}
