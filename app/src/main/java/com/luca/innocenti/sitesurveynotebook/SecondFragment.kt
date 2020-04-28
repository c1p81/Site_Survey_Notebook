package com.luca.innocenti.sitesurveynotebook

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileInputStream


private lateinit var mContext: Context


class Sito {
    var emp_id: Int? = 0
    var emp_name: String? = null
    var emp_photo: String? = null
}

class MyListAdapter (private var activity: Activity, private var items: ArrayList<Sito>) :  BaseAdapter(){
    private class ViewHolder(row: View?) {
        var img_p: ImageView? = null
        var nome: TextView? = null
        init {
            this.img_p = row?.findViewById<ImageView>(R.id.img)
            this.nome = row?.findViewById<TextView>(R.id.data)
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.sito, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        var emp = items[position]
        viewHolder.nome?.text = emp.emp_name
        //viewHolder.img_p?.setImageResource(emp.emp_photo!!)

        Log.d("imgFile", emp.emp_photo!!)
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permesso","NON NON ")
        }




        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        Log.d("emp2",emp.emp_photo)



        val destination = File(emp.emp_photo)
        val fileInputStream: FileInputStream
        fileInputStream = FileInputStream(destination)
        val ur = BitmapFactory.decodeStream(fileInputStream)
        viewHolder.img_p?.setImageBitmap(ur)


        return view as View
    }
    override fun getItem(i: Int): Sito {
        return items[i]
    }
    override fun getItemId(i: Int): Long {
        return i.toLong()
    }
    override fun getCount(): Int {
        return items.size
    }
}


class SecondFragment : Fragment() {


    private var lista: ListView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lista = view.findViewById<ListView>(R.id.listadir) as ListView

        var adapter : MyListAdapter? = null

        var result = ArrayList<Sito>()
        //result = generadati()

        result = dirdata()
        adapter = MyListAdapter(requireActivity(), result)
        lista!!.adapter = adapter

        lista!!.setOnItemClickListener { parent, view, position, id ->
            //val element = adapter.getItemAtPosition(position) // The item that was clicked


            val dialog = AlertDialog.Builder(mContext).setTitle("Help").setMessage("To record the audio, press and hold the button. After taking the photo and audio recording, press the + button to confirm.")
                //.setPositiveButton("Ok", { dialog, i -> })
                .setPositiveButton("View") { dialog, which ->
                                    Log.d("View","View")
                                    Log.d("View","View")
                                    val intent = Intent(activity, Main2Activity::class.java)
                                    intent.putExtra("s", result.get(position).emp_name)

                                    startActivity(intent)
                                    }
                .setNeutralButton("Delete") { dialog, which ->
                    Log.d("Delete","Delete")
                    Log.d("Delete","Delete")
                    var dir=File(mContext.getExternalFilesDir("SiteSurvey/"+result.get(position).emp_name),"")

                    Log.d("delete", dir.toString())
                    if (dir.isDirectory) {
                        val children: Array<String> = dir.list()
                        for (i in children.indices) {
                            File(dir, children[i]).delete()
                        }
                    }
                    dir.delete()
                    result.clear()
                    result = dirdata()

                    adapter = MyListAdapter(requireActivity(), result)
                    lista!!.adapter = adapter

                    //adapter.notifyDataSetChanged()
                    //lista!!.invalidateViews();

                }



            dialog.show()
        }

    }



    // scasioni i nomi delle directory e crea i dati per la listview
    private fun dirdata(): ArrayList<Sito>
    {
        var result = ArrayList<Sito>()
        var emp: Sito = Sito()

        val path = File(mContext.getExternalFilesDir("SiteSurvey"),"").toString()
        Log.d("Files", "Path: $path")
        val directory = File(path)
        val files = directory.listFiles()
        Log.d("Files", "Size: " + files.size)
        for (i in files.indices) {
            Log.d("Files", "FileName:" + files[i].name)
            if (files[i].name != "tmp") {
                var emp: Sito = Sito()
                emp.emp_name = files[i].name
                emp.emp_photo = path + "/" + files[i].name + "/" + files[i].name + ".jpg"
                result.add(emp)
            }
        }
        return result
    }



    override fun onAttach(context: Context) {
        super.onAttach(context);
        mContext=context;

    }

    override fun onResume() {
        super.onResume()

        if (view == null) {
            return
        }
        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener { v, keyCode, event ->
            if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                // handle back button's click listener
                //Log.d("Back","1")

                Toast.makeText(mContext, "Use the menu to go back", Toast.LENGTH_SHORT).show()
                true
            } else false
        }

    }


}
