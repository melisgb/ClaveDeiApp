package com.example.myandroidapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myandroidapp.Adapter.ListOfSongsPerListAdapter
import com.example.myandroidapp.Model.SongsList
import com.example.myandroidapp.db.DatabaseHelper
import kotlinx.android.synthetic.main.activity_view_list.*
import kotlinx.android.synthetic.main.list_elem_view_songs_list.*

class ViewListActivity : AppCompatActivity() {

    internal lateinit var db: DatabaseHelper
    var adapter : ListOfSongsPerListAdapter? = null
    var list_id : Int? = null

    companion object {
        const val EXTRA_LIST_ID = "list_id"
    }
    // To hide keyboard
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_list)

        db = DatabaseHelper(this)
        val listId_extra = this.intent.getIntExtra(EXTRA_LIST_ID, 0)
        val current_SongsList: SongsList? = db.getSongsList(listId_extra)

        listTitleTxtView.setText(current_SongsList!!.name)

        adapter = ListOfSongsPerListAdapter(this, current_SongsList!!.songs.values.toList())
        songsFromListLstView.adapter = adapter


        val myToast = Toast.makeText(applicationContext, "LIST VIEW OPENED", Toast.LENGTH_SHORT)
        myToast.setGravity(Gravity.BOTTOM, 0, 50)
        myToast.show()


        songsFromListLstView.setOnItemClickListener { parent, view, position, longID ->
            val id = longID.toInt()
            val intent = Intent(this, CreateSongActivity::class.java)
            intent.putExtra(CreateSongActivity.EXTRA_SONG_ID, id)
            startActivity(intent)
        }


        songsFromListLstView.setOnItemLongClickListener { parent, view, position, longID ->
            val id = longID.toInt()
            val res = showSongPopUpMenu(view, listId_extra , id)
            true
        }



    }
    fun refreshAll() {
        adapter?.notifyDataSetChanged()
    }

    @SuppressLint("NewApi")
    fun showSongPopUpMenu(v : View, listID : Int?, songID : Int){
        val popup = PopupMenu(this, v, Gravity.RIGHT )
        popup.setOnMenuItemClickListener { item ->
             when (item?.itemId) {
                 R.id.option_song_delete -> {
                     db.deleteSongsList(songID, db.getSongsList(listID!!))
                     adapter?.listOfSongsPerList = db.getSongsList(listID).songs.values.toList()
                     refreshAll()
                     true
                 }
                 R.id.option_song_share -> {
//                    implement the share function
                    val sharingIntent = Intent(Intent.ACTION_SEND)
                    sharingIntent.setType("text/plain")
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Cancion ID: ${songID}")

                    val stringOfSong = ArrayList<String>()
                     stringOfSong.add("${songID} : ${db.getSong(songID)?.title}")

                    sharingIntent.putExtra(Intent.EXTRA_TEXT, stringOfSong.toString())
                    startActivity(Intent.createChooser(sharingIntent, "Share via"))

                    true
                }

                else ->
                    false
            }
        }
        popup.inflate(R.menu.song_popup_menu)
        popup.setForceShowIcon(true)
        popup.show()
    }


}

