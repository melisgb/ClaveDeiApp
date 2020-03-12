package com.example.myandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.myandroidapp.db.DatabaseHelper
import kotlinx.android.synthetic.main.activity_search_songs_by_parts.*
import kotlinx.android.synthetic.main.activity_search_songs_by_season.*

class SearchSongsBySeasonsActivity : AppCompatActivity() {
    internal lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_songs_by_season)

        db = DatabaseHelper(this)

        s1_Ordinario_btn.setOnClickListener{
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Ordinario")
            startActivity(intent)
        }

        s2_Adviento_btn.setOnClickListener{
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Adviento")
            startActivity(intent)
        }

        s3_Navidad_btn.setOnClickListener{
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Navidad")
            startActivity(intent)
        }

        s4_Cuaresma_btn.setOnClickListener{
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Cuaresma")
            startActivity(intent)
        }

        s5_Pascua_btn.setOnClickListener{
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Pascua")
            startActivity(intent)
        }

        s6_Pentecostes_btn.setOnClickListener{
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Pentecostes")
            startActivity(intent)
        }
    }
}
