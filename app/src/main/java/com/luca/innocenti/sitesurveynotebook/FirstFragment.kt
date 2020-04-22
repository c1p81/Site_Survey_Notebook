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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.camerakit.CameraKitView
import com.camerakit.CameraKitView.ImageCallback
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

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

    private fun scatta()
    {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraKitView = view.findViewById(R.id.camera);

        output = File(mContext.getExternalFilesDir("SiteSurvey"), "tmp_recording.mp3").toString()

        Log.d("output",output)
        mediaRecorder = MediaRecorder()
        mediaPlayer = MediaPlayer.create(mContext,R.raw.ding)

        val shutter = view.findViewById<ImageButton>(R.id.shutter)
        shutter.setOnClickListener{
            Log.d("shutter","shu")
            cameraKitView?.captureImage(ImageCallback { cameraKitView, capturedImage ->
                val savedPhoto =File(mContext.getExternalFilesDir("SiteSurvey"), "tmp_photo.jpg")
                Log.d("Photo",savedPhoto.toString())
                try {
                    val outputStream = FileOutputStream(savedPhoto.path)
                    outputStream.write(capturedImage)
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            })
        }




        view.findViewById<ImageButton>(R.id.save).setOnTouchListener { View, event ->
            val action = event.action
            when(action) {
                MotionEvent.ACTION_DOWN -> {
                    if (ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        ActivityCompat.requestPermissions(mContext as Activity, permissions,0)
                    } else {
                        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
                        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                        mediaRecorder?.setOutputFile(output)
                        mediaRecorder?.prepare()
                        mediaRecorder?.start()
                        mediaPlayer.start()
                        Toast.makeText(mContext, "Recording started!", Toast.LENGTH_SHORT).show()
                        }


                    Log.d("Evento","Premuto")
                }
                MotionEvent.ACTION_UP -> {
                    try{
                        mediaRecorder?.stop()
                        mediaRecorder?.release()
                        Toast.makeText(mContext, "Stop Recording", Toast.LENGTH_SHORT).show()
                        mediaPlayer.start()
                    }catch (e: IllegalStateException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    Log.d("Evento","Rilasciato")
                }
            }
            true
        }

    }


}
