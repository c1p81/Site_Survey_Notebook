package com.luca.innocenti.sitesurveynotebook

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.camerakit.CameraKitView
import com.camerakit.CameraKitView.ImageCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.NonCancellable.start
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.luca.innocenti.sitesurveynotebook.variabili.Companion.audio
import com.luca.innocenti.sitesurveynotebook.variabili.Companion.photo
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var icona_camera: ImageView? = null
    private var icona_audio: ImageView? = null
    private lateinit var stato: variabili
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mContext: Context
    private lateinit var mediaRecorder: MediaRecorder
    private var output: String? = null

    private var cameraKitView: CameraKitView? = null


    override fun onAttach(context: Context) {
        super.onAttach(context);
        mContext=context;
        //this.listener = context as? FragmentListener
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }


    override fun onResume() {
        super.onResume()
        cameraKitView?.onResume()
    }

    override fun onStart ()
    {
        super.onStart()
        cameraKitView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        cameraKitView?.onStop()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraKitView = view.findViewById(R.id.camera);
        stato = variabili()
        output = File(mContext.getExternalFilesDir("SiteSurvey/tmp"), "tmp_recording.mp3").toString()

        Log.d("output",output)
        mediaRecorder = MediaRecorder()

        mediaPlayer = MediaPlayer.create(mContext,R.raw.ding)

        val shutter = view.findViewById<ImageButton>(R.id.shutter)
        val aggiungi = view.findViewById<ImageButton>(R.id.imageButton3)
        icona_camera = view.findViewById<ImageView>(R.id.camera_icon)
        icona_audio = view.findViewById<ImageView>(R.id.audio_icon)
        icona_camera?.visibility = View.INVISIBLE
        icona_audio?.visibility = View.INVISIBLE


        aggiungi.setOnClickListener{
            Log.d("shutter","shu")
            //Log.d("variabili_main", stato.audio.toString()+ " "+stato.photo.toString())
            //if (true)
            if (audio && photo)
            {
                // crea sposta i dati dal tmeporaneo alla directory
                Log.d("Directory","Sposta")
                val sdf = SimpleDateFormat("dd_MM_yyyy_HH:mm:ss")
                val currentDate = sdf.format(Date())
                val folder_main = currentDate

                val f =
                    File(mContext.getExternalFilesDir("SiteSurvey"), folder_main)
                if (!f.exists()) {
                    f.mkdirs()
                }
                var mp3_tmp:File = File(mContext.getExternalFilesDir("SiteSurvey/tmp"), "tmp_recording.mp3")
                var jpg_tmp:File= File(mContext.getExternalFilesDir("SiteSurvey/tmp"), "tmp_photo.jpg")
                Log.d("file", mp3_tmp.toString())
                Log.d("file", jpg_tmp.toString())

                var mp3_def: File = File(mContext.getExternalFilesDir("SiteSurvey/"+currentDate), currentDate+".mp3")
                var jpg_def: File = File(mContext.getExternalFilesDir("SiteSurvey/"+currentDate), currentDate+".jpg")

                Log.d("file", mp3_def.toString())
                Log.d("file", jpg_def.toString())

                mp3_tmp.copyTo(mp3_def,true)
                jpg_tmp.copyTo(jpg_def,true)
                audio = false
                photo = false
                icona_camera?.visibility = View.INVISIBLE
                icona_audio?.visibility= View.INVISIBLE
                mp3_tmp.delete()
                jpg_tmp.delete()

            }
            else
            {
                Snackbar.make(view, "First take a photo and record the audio note", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }

        }




        shutter.setOnClickListener{
            Log.d("shutter","shu")

            cameraKitView?.captureImage(ImageCallback { cameraKitView, capturedImage ->
                val savedPhoto =File(mContext.getExternalFilesDir("SiteSurvey/tmp"), "tmp_photo.jpg")
                Log.d("Photo",savedPhoto.toString())
                try {
                    val outputStream = FileOutputStream(savedPhoto.path)
                    outputStream.write(capturedImage)
                    outputStream.close()
                    photo = true
                    mediaPlayer.start()
                    icona_camera?.visibility = View.VISIBLE
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.d("Camera", "errore" + e.toString())
                }
            })
        }

        view.findViewById<ImageButton>(R.id.clear).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                photo = false
                audio = false
                icona_camera?.visibility = View.INVISIBLE
                icona_audio?.visibility= View.INVISIBLE
                Log.d("Clear","clear global variables")
            }
        })



        view.findViewById<ImageButton>(R.id.save).setOnTouchListener { View, event ->
            val action = event.action
            when(action) {
                MotionEvent.ACTION_DOWN -> {
                        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
                        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                        mediaRecorder?.setOutputFile(output)
                        mediaRecorder?.prepare()
                        mediaRecorder?.start()
                        mediaPlayer.start()
                        audio = true
                        icona_audio?.visibility = android.view.View.VISIBLE
                       Toast.makeText(mContext, "Recording started!", Toast.LENGTH_SHORT).show()
                    Log.d("Evento","Premuto")
                }
                MotionEvent.ACTION_UP -> {
                    try{
                        mediaRecorder?.stop()
                        //mediaRecorder?.release()
                        Toast.makeText(mContext, "Stop Recording", Toast.LENGTH_SHORT).show()
                        mediaPlayer.start()
                    }catch (e: IllegalStateException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    mediaPlayer.stop()
                    mediaPlayer.release()

                    Log.d("Evento","Rilasciato")
                }
            }
            true
        }

    }


}
