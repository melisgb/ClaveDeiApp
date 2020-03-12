package com.example.myandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myandroidapp.db.DatabaseHelper

//const val EXTRA_MESSAGE = "com.example.myandroidapp.MESSAGE"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//    fun sendMessage(view: View){
//        val editText = findViewById<EditText>(R.id.editText)
//        val message = editText.text.toString()
//        val intent = Intent(this, SearchSongsBySeasonsActivity::class.java ).apply{
//            putExtra(EXTRA_MESSAGE, message)/*adds the value of the EditText field to an intent, specifically the extra_message*/
//        }
//        startActivity(intent)
//    }
        val searchAllButton = findViewById<Button>(R.id.buscarCancionesMBtn)
        searchAllButton.setOnClickListener{
            val intent = Intent(this, SearchSongsActivity::class.java)
            startActivity(intent)
        }

        val byPartsButton = findViewById<Button>(R.id.verCatalogoPartesMBtn)
            byPartsButton.setOnClickListener{
            val intent = Intent(this, SearchSongsByPartsActivity::class.java)
            startActivity(intent)
        }

        val bySeasonButton = findViewById<Button>(R.id.verCatalogoTiemposMBtn)
            bySeasonButton.setOnClickListener{
                val intent = Intent(this, SearchSongsBySeasonsActivity::class.java)
            startActivity(intent)
            }

        val myListButton = findViewById<Button>(R.id.verMiListaMBtn)
        myListButton.setOnClickListener{
            val intent = Intent(this, ReadMyListActivity::class.java)
            startActivity(intent)
        }

        val createSongButton = findViewById<Button>(R.id.ingresarCancionMBtn)
        createSongButton.setOnClickListener {
            val intent = Intent(this, CreateSongActivity::class.java)
            startActivity(intent)
        }


        var databaseHelper = DatabaseHelper(this)



    }
}


