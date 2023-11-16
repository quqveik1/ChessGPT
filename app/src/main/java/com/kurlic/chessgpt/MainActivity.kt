package com.kurlic.chessgpt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kurlic.chessgpt.localgames.LocalGameDao

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    var localGameDao: LocalGameDao? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)
    }
}