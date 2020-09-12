package com.example.firestoreinsetprototype

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        student_button.setOnClickListener {
            val intent = Intent(this, StudentActivity::class.java)
            startActivity(intent)
        }

        lecturer_button.setOnClickListener {
            val intent = Intent(this, LecturerActivity::class.java)
            startActivity(intent)
        }

        module_button.setOnClickListener{
            val intent = Intent(this,ModuleActivity::class.java)
            startActivity(intent)
        }
    }
}