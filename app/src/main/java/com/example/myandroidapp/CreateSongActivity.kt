package com.example.myandroidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.myandroidapp.Model.Song
import com.example.myandroidapp.db.DatabaseHelper
import kotlinx.android.synthetic.main.activity_create_song.*
import kotlin.random.Random

class CreateSongActivity : AppCompatActivity() {

    internal lateinit var db: DatabaseHelper
    var song_id : Int? = null

    companion object {
        const val EXTRA_SONG_ID = "song_id"
    }
    // To hide keyboard
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun fillSong() {
        val songId_extra = this.intent.getIntExtra(EXTRA_SONG_ID, 0)
        val current_Song: Song? = db.getSong(songId_extra)

        if (songId_extra != 0) {
            song_id = current_Song?.id
            songTitle_editTxt.setText(current_Song?.title)
            songArtist_editTxt.setText(current_Song?.artist)
            songLyrics_editTxt.setText(current_Song?.lyrics)
            songTags_editTxt.setText(current_Song?.tags)
            createSong_btn.isEnabled = false
            createSong_btn.visibility = View.INVISIBLE
            createSongTitle_TxtView.text = "MODIFICAR CANCION"
        }
        else{
            updateSong_btn.isEnabled = false
            updateSong_btn.visibility = View.INVISIBLE
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_song)

        db = DatabaseHelper(this)
//        populateSongs(15)
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

            val intent = Intent(this, ViewSongActivity::class.java)
            intent.putExtra(ViewSongActivity.EXTRA_SONG_ID, song_id!!)
            startActivity(intent)
        }

//        deleteSong_btn.setOnClickListener {
//            val song = Song(
//                song_id!!,
//                songTitle_editTxt.text.toString(),
//                songArtist_editTxt.text.toString(),
//                songLyrics_editTxt.text.toString(),
//                songTags_editTxt.text.toString()
//            )
//
//            var songDeleted = true
//            try{
//                db.deleteSong(song)
//            }
//            catch(e: IllegalStateException){
////                showImpossibleSongDeletion()
//                songDeleted = false
//                val myToast = Toast.makeText(this, R.string.unable_to_delete_this_song,  Toast.LENGTH_LONG)
//                myToast.setGravity(Gravity.BOTTOM,0,50)
//                myToast.show()
//            }
//            finally {
//                if(songDeleted) {
//                    val myToast = Toast.makeText(this, R.string.toast_song_deleted,  Toast.LENGTH_SHORT)
//                    myToast.setGravity(Gravity.BOTTOM, 0, 50)
//                    myToast.show()
//                }
//            }
//
//            val intent = Intent(this, SearchSongsActivity::class.java)
//            startActivity(intent)
//
//
//        }
    }


    fun populateSongs(qty: Int) {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val tags = arrayOf<String>( "Ordinario", "Adviento", "Navidad", "Cuaresma", "Pascua", "Pentecostes",
            "Entrada", "Piedad", "Gloria", "Salmo", "Proclamacion", "Ofertorio", "Paz", "Cordero", "Comunion", "Despedida")
        val rNames = arrayOf<String>( "Maria", "Gloria", "Alegre", "Jesucristo", "Soldado de", "Pentecostes",
            "Misericordia", "va conmigo", "del camino", "casa", "Senor", "Acompaname")
        val STRING_LENGTH = 5;


        for(x in 0..qty){
            val randomString = (1..STRING_LENGTH)
                .map { i -> Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")

            val song = Song(
                -1,
                rNames.random() + " " + rNames.random(),
                randomString,
                randomString,
                tags.random()+ ", " + tags.random()
            )
            db.addSong(song)
        }
    }
}