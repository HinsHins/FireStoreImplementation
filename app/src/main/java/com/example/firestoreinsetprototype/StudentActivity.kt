package com.example.firestoreinsetprototype

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.firestoreinsetprototype.Adaptor.StudentAdaptor
import com.example.firestoreinsetprototype.Model.Student
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_student.*

class StudentActivity : AppCompatActivity() {
    private val students = ArrayList<Student>()
    var fb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        student_list_view.adapter = StudentAdaptor(this,R.layout.student_listview_item,students)

        student_list_view.setOnItemLongClickListener { adapterView, view, i, l ->
            Log.d("OnItemLongClickListener","long pressed at $i and id $l and the view $view")
            presentDeleteAlert(students[i])
            return@setOnItemLongClickListener(true)
        }

        student_insert.setOnClickListener {
            var id = sid_et.text.toString().trim()
            var name = sname_et.text.toString().trim()
            var email = semail_et.text.toString().trim()
            val programme = sprogramme_et.text.toString().trim()
            val country = scountry_et.text.toString().trim()

            if (id != "" && name != "" && email != "" && programme != "" && country != "") {
                var student = Student(id, name, email, programme, country)
                Log.d("Student", "$student")
                writeStudent(student)
                Toast.makeText(this@StudentActivity, "Insert successful", Toast.LENGTH_SHORT)
                    .show()
            } else
                Toast.makeText(
                    this@StudentActivity,
                    "Please fill all fields before insert",
                    Toast.LENGTH_SHORT
                ).show()
        }

        val studentRef = fb.collection("students")
        studentRef.get()
            .addOnSuccessListener { result->
                for(document in result){
                    Log.d("Student", "${document.id} => ${document.data}")
                    var student = document.toObject(Student::class.java)
                    Log.d("Student","$student")
                    students.add(student)
                }
                Log.d("load Student", "$students")
                (student_list_view.adapter as StudentAdaptor).notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d("", "Error getting documents: ", exception)
            }
    }

    private fun presentDeleteAlert(student: Student) {
        val dialog = AlertDialog.Builder(this)
        dialog.setCancelable(true).setMessage("Are you sure you want to delete this Student? ${student.id}")
            .setPositiveButton("Yes",DialogInterface.OnClickListener { dialogInterface, i ->
                deleteStudent(student)
            })

    }

    private fun deleteStudent(student: Student) {
       val studentRef = fb.collection("students")
        realTimeUpdate(studentRef)
        studentRef.document(student.id).delete()
            .addOnSuccessListener {
                Log.d("", "Student successfully deleted!")
            }
            .addOnFailureListener { e->
                Log.w("", "Error deleting document",e )
            }
    }

    private fun writeStudent(student: Student) {
        val studentRef = FirebaseFirestore.getInstance().collection("students")
        realTimeUpdate(studentRef)
        FirebaseFirestore.getInstance().collection("students")
            .document(student.id)
            .set(student)
            .addOnSuccessListener {
                Log.d("", "Student successfully written!")
            }
            .addOnFailureListener {e->
                Log.w("", "Error writing document", e)
            }
    }

    private fun realTimeUpdate(studentRef:CollectionReference){
        studentRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Fail", "Listen failed.", e)
                return@addSnapshotListener
            }

            val source = if (snapshot != null && snapshot.metadata.hasPendingWrites())
                "Local"
            else
                "Server"

            if (snapshot != null) {
                for (dc in snapshot.documentChanges) {
                   val doc = dc.document.toObject(Student::class.java)
                    Log.d("dc.type", dc.type.toString())
                    when(dc.type){
                        DocumentChange.Type.ADDED -> {
                            if (hasStudent(students, doc) == null) {
                                students.add(doc)
                                Log.d("adding student",doc.toString())
                            }
                        }
                        DocumentChange.Type.MODIFIED -> {
                            hasStudent(students, doc).let { student ->
                                val index = students.indexOf(student)
                                students[index] = doc
                                Log.d("Modified Student", doc.toString())
                            }
                        }
                        DocumentChange.Type.REMOVED -> {
                            hasStudent(students, doc).let { student ->
                                students.remove(student)
                                Log.d("Removing student",student.toString())

                            }
                        }
                    }
                }
                Log.d("RealTimeUpdate", "$students")
                (student_list_view.adapter as StudentAdaptor).notifyDataSetChanged()
            } else {
                android.util.Log.d("null", "$source data: null")
            }
        }

    }

    private fun hasStudent(arrayList: ArrayList<Student>, student: Student):Student?{
        for(i in 0 until arrayList.size){
            if(student.id==arrayList[i].id){
                return arrayList[i]
            }
        }
        return null
    }
}