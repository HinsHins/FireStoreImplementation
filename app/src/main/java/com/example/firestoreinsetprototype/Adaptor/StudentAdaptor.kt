package com.example.firestoreinsetprototype.Adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.firestoreinsetprototype.Model.Student
import com.example.firestoreinsetprototype.R
import kotlinx.android.synthetic.main.student_listview_item.view.*
import org.w3c.dom.Text

class StudentAdaptor(var mCtx: Context,var resource:Int,var items:List<com.example.firestoreinsetprototype.Model.Student>):ArrayAdapter<Student>(mCtx,resource,items){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater:LayoutInflater = LayoutInflater.from(mCtx)
        val view = layoutInflater.inflate(resource,null)

        val sid_tv: TextView = view.findViewById(R.id.sid_tv)
        val sname_tv:TextView = view.findViewById(R.id.student_name_tv)
        val email_tv:TextView = view.findViewById(R.id.student_email_tv)
        val programme_tv:TextView = view.findViewById(R.id.programme_tv)
        val country_tv:TextView = view.findViewById(R.id.country_tv)

        var student:Student = items[position]
        sid_tv.text = student.id.toString()
        sname_tv.text = student.name
        email_tv.text = student.email
        programme_tv.text = student.programme
        country_tv.text = student.country

        return view
    }
}