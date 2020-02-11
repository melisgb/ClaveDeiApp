package com.example.myandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myandroidapp.Adapter.ListSongsAdapter
import com.example.myandroidapp.Model.Song
import com.example.myandroidapp.db.DatabaseHelper
import kotlinx.android.synthetic.main.activity_search_songs.*
import kotlinx.android.synthetic.main.list_elem_search_songs.view.*

class SearchSongsActivity : AppCompatActivity() {

    internal lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_songs)

        db = DatabaseHelper(this)

        val listSongs = db.getSongs()
        val adapter =  ListSongsAdapter(this, listSongs)
        songs_lstView.adapter = adapter


        songs_lstView.setOnItemClickListener{ p, v, pos, id ->
            val intent = Intent(this, CreateSongActivity::class.java)
            intent.putExtra(CreateSongActivity.EXTRA_SONG_ID, id)
            startActivity(intent)

        }


//        //Event
//        songView.setOnClickListener{
//            songId_editTxt.setText(songView.songIdTxtView.text.toString())
//            songTitle_editTxt.setText(songView.songTitleTxtView.text)
//            songArtist_editTxt.setText(songView.songArtistTxtView.text)
//            songLyrics_editTxt.setText(songView.songLyricsTxtView.text)
//            songTags_editTxt.setText(songView.songTagsTxtView.text)
//        }
//        songs_lstView.adapter

//        refreshData()
    }

//    private fun refreshData(){
//        listSongs = db.allSongs
//        val adapter = ListSongsAdapter(this, listSongs, songId_editTxt, songTitle_editTxt, songArtist_editTxt, songLyrics_editTxt, songTags_editTxt)
//        listSongs_listView.adapter = adapter //lista de personas de primer view
//    }
}
