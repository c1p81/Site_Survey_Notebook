package com.luca.innocenti.sitesurveynotebook

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.luca.innocenti.sitesurveynotebook.R.id.listadir
import kotlinx.android.synthetic.main.fragment_second.*

class Sito {
    var emp_id: Int? = 0
    var emp_name: String? = null
    var emp_designation: String? = null
    var emp_salary: String? = null
    var emp_photo: Int? = null
}

class MyListAdapter (private var activity: Activity, private var items: ArrayList<Sito>) :  BaseAdapter(){
    private class ViewHolder(row: View?) {
        var imgEmp: ImageView? = null
        var lblSalary: TextView? = null
        init {
            this.imgEmp = row?.findViewById<ImageView>(R.id.img_emp)
            this.lblSalary = row?.findViewById<TextView>(R.id.data)
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
        viewHolder.lblSalary?.text = emp.emp_salary
        viewHolder.imgEmp?.setImageResource(emp.emp_photo!!)

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
    private lateinit var mContext: Context

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
        result = generadati()
        adapter = MyListAdapter(requireActivity(), result)
        lista!!.adapter = adapter

    }

    private fun generadati(): ArrayList<Sito> {
        var result = ArrayList<Sito>()
        var emp: Sito = Sito()
        emp.emp_id = 1
        emp.emp_name = "John Clington"
        emp.emp_designation = "CEO"
        emp.emp_salary = "USD 21000$"
        //emp.emp_photo = R.drawable.p1
        result.add(emp)
        return  result
    }

    override fun onAttach(context: Context) {
        super.onAttach(context);
        mContext=context;
        //this.listener = context as? FragmentListener
    }
}
