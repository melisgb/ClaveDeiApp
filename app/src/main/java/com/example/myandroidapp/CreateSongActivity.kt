package com.example.myandroidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.myandroidapp.Model.Song
import com.example.myandroidapp.db.DatabaseHelper
import kotlinx.android.synthetic.main.activity_create_song.*
import kotlinx.android.synthetic.main.activity_search_songs.*
import kotlinx.android.synthetic.main.list_elem_search_songs.view.*

class CreateSongActivity : AppCompatActivity() {

    internal lateinit var db: DatabaseHelper
    var song_id : Int? = null

    companion object {
        const val EXTRA_SONG_ID = "song_id"
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun fillSong() {
        val songId_extra: Long = this.intent.getLongExtra(EXTRA_SONG_ID, 0)
        val current_Song: Song? = db.getSong(songId_extra)

        if (songId_extra != 0L) {
            song_id = current_Song?.id
            songTitle_editTxt.setText(current_Song?.title)
            songArtist_editTxt.setText(current_Song?.artist)
            songLyrics_editTxt.setText(current_Song?.lyrics)
            songTags_editTxt.setText(current_Song?.tags)
            createSong_btn.isEnabled = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_song)

        db = DatabaseHelper(this)
        fillSong()


        //Event to add a song to a list
        createSong_btn.setOnClickListener {
            val song = Song(
                -1,
                songTitle_editTxt.text.toString(),
                songArtist_editTxt.text.toString(),
                songLyrics_editTxt.text.toString(),
                songTags_editTxt.text.toString()
            )
            db.addSong(song)


            val myToast = Toast.makeText(applicationContext, R.string.toast_song_created, Toast.LENGTH_SHORT)
            myToast.setGravity(Gravity.BOTTOM, 0, 50)
            myToast.show()

            val intent = Intent(this, SearchSongsActivity::class.java)
            startActivity(intent)

        }

        updateSong_btn.setOnClickListener {
            val song = Song(
                song_id!!,
                songTitle_editTxt.text.toString(),
                songArtist_editTxt.text.toString(),
                songLyrics_editTxt.text.toString(),
                songTags_editTxt.text.toString()
            )
            db.updateSong(song)

            val myToast = Toast.makeText(applicationContext, R.string.toast_song_updated,  Toast.LENGTH_SHORT)
            myToast.setGravity(Gravity.BOTTOM, 0, 50)
            myToast.show()

            val intent = Intent(this, SearchSongsActivity::class.java)
            startActivity(intent)
        }

        deleteSong_btn.setOnClickListener {
            val song = Song(
                song_id!!,
                songTitle_editTxt.text.toString(),
                songArtist_editTxt.text.toString(),
                songLyrics_editTxt.text.toString(),
                songTags_editTxt.text.toString()
            )
            db.deleteSong(song)

            val myToast = Toast.makeText(applicationContext, R.string.toast_song_deleted,  Toast.LENGTH_SHORT)
            myToast.setGravity(Gravity.BOTTOM, 0, 50)
            myToast.show()

            val intent = Intent(this, SearchSongsActivity::class.java)
            startActivity(intent)
        }
    }
}