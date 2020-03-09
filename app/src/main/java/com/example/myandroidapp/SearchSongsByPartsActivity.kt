package com.example.myandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myandroidapp.db.DatabaseHelper
import kotlinx.android.synthetic.main.activity_search_songs_by_parts.*

class SearchSongsByPartsActivity : AppCompatActivity() {
    internal lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_songs_by_parts)

        db = DatabaseHelper(this)

        p1_Entrada_btn.setOnClickListener{
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Entrada")
            startActivity(intent)
        }

        p2_Piedad_btn.setOnClickListener {
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Piedad")
            startActivity(intent)
        }

        p3_Gloria_btn.setOnClickListener {
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Gloria")
            startActivity(intent)
        }

        p4_Salmos_btn.setOnClickListener {
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Salmos")
            startActivity(intent)
        }

        p5_Proclamacion_btn.setOnClickListener {
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Aleluya")
            startActivity(intent)
        }

        p6_Ofertorio_btn.setOnClickListener {
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Ofertorio")
            startActivity(intent)
        }

        p7_Paz_btn.setOnClickListener {
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Paz")
            startActivity(intent)
        }

        p8_Cordero_btn.setOnClickListener {
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Cordero")
            startActivity(intent)
        }

        p9_Comunion_btn.setOnClickListener {
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Comunion")
            startActivity(intent)
        }

        p10_Despedida_btn.setOnClickListener {
            val intent = Intent(this, SearchSongsActivity::class.java)
            intent.putExtra(SearchSongsActivity.EXTRA_KEYWORD, "Despedida")
            startActivity(intent)
        }

    }
}

