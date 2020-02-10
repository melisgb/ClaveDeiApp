package com.example.myandroidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.example.myandroidapp.Adapter.ListSongsAdapter
import com.example.myandroidapp.Model.Song
import com.example.myandroidapp.db.DatabaseHelper
import kotlinx.android.synthetic.main.activity_create_song.*

class CreateSongActivity : AppCompatActivity() {

    internal lateinit var db: DatabaseHelper
    internal var listSongs:List<Song> = ArrayList<Song>()

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_song)

        db = DatabaseHelper(this)
        refreshData()


        //Event to add a song to a list
        createSong_btn.setOnClickListener{
            val song = Song(
                Integer.parseInt(songId_editTxt.text.toString()),
                songTitle_editTxt.text.toString(),
                songArtist_editTxt.text.toString(),
                songLyrics_editTxt.text.toString(),
                songTags_editTxt.text.toString()
            )
            db.addSong(song)
            refreshData()
        }

        updateSong_btn.setOnClickListener {
            val song = Song(
                Integer.parseInt(songId_editTxt.text.toString()),
                songTitle_editTxt.text.toString(),
                songArtist_editTxt.text.toString(),
                songLyrics_editTxt.text.toString(),
                songTags_editTxt.text.toString()
            )
            db.updateSong(song)
            refreshData()
        }

        deleteSong_btn.setOnClickListener {
            val song = Song(
                Integer.parseInt(songId_editTxt.text.toString()),
                songTitle_editTxt.text.toString(),
                songArtist_editTxt.text.toString(),
                songLyrics_editTxt.text.toString(),
                songTags_editTxt.text.toString()
            )
            db.deleteSong(song)
            refreshData()
        }



    }

    private fun refreshData(){
        listSongs = db.allSongs
        val adapter = ListSongsAdapter(this, listSongs, songId_editTxt, songTitle_editTxt, songArtist_editTxt, songLyrics_editTxt, songTags_editTxt)
        listSongs_listView.adapter = adapter //lista de personas de primer view
    }
}


