package com.example.revision2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class KotlinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
var a=1;

        val t=findViewById<TextView>(R.id.ktview);
        t.setText("djd");
    }
}