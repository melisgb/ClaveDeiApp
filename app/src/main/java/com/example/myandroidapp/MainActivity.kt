package com.example.myandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myandroidapp.db.DatabaseHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchAllButton = findViewById<Button>(R.id.buscarCancionesMBtn)
        searchAllButton.setOnClickListener{
            val intent = Intent(this, SearchSongsActivity::class.java)
            startActivity(intent)
        }

        val searchByPartsButton = findViewById<Button>(R.id.verCatalogoPartesMBtn)
        searchByPartsButton.setOnClickListener{
            val intent = Intent(this, SearchSongsByPartsActivity::class.java)
            startActivity(intent)
        }

        val searchBySeasonButton = findViewById<Button>(R.id.verCatalogoTiemposMBtn)
        searchBySeasonButton.setOnClickListener{
                val intent = Intent(this, SearchSongsBySeasonsActivity::class.java)
            startActivity(intent)
            }

        val viewMyListsButton = findViewById<Button>(R.id.verMiListaMBtn)
        viewMyListsButton.setOnClickListener{
            val intent = Intent(this, ReadListsActivity::class.java)
            startActivity(intent)
        }

        val createSongButton = findViewById<Button>(R.id.ingresarCancionMBtn)
        createSongButton.setOnClickListener {
            val intent = Intent(this, CreateSongActivity::class.java)
            startActivity(intent)
        }

    }
}


