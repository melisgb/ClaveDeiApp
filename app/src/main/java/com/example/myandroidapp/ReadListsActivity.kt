package com.example.myandroidapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myandroidapp.Adapter.ListsOfSongsListAdapter
import com.example.myandroidapp.Model.SongsList
import com.example.myandroidapp.db.DatabaseHelper
import kotlinx.android.synthetic.main.activity_read_lists.*


class ReadListsActivity : AppCompatActivity() {

    internal lateinit var db: DatabaseHelper
    var adapter : ListsOfSongsListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_lists)


        db = DatabaseHelper(this)

        val songsLists = db.getSummaryLists()
        songsLists.sortedBy { t -> t.name }
        adapter = ListsOfSongsListAdapter(this, songsLists)
        myListsListView.adapter = adapter

        myListsListView.setOnItemClickListener { parent, view, position, longID ->
            val id = longID.toInt()
            val intent = Intent(this, ViewListActivity::class.java)
            intent.putExtra(ViewListActivity.EXTRA_LIST_ID, id)
            startActivity(intent)


        }

        myListsListView.setOnItemLongClickListener { parent, view, position, longID ->
            val id = longID.toInt()
            val res = showPopUpMenu(view, db.getSongsList(id))
            true
        }



    }

    override fun onResume() {
        refreshAll()
        super.onResume()
    }

    fun refreshAll() {
        adapter?.listOfSongsList = db.getSummaryLists()
        adapter?.notifyDataSetChanged()
    }


    @SuppressLint("NewApi")
    fun showPopUpMenu(v : View, list : SongsList){
        val popup = PopupMenu(this, v, Gravity.RIGHT )
        popup.setOnMenuItemClickListener { item ->
             when (item?.itemId) {
                R.id.list_option_1 -> {
                    Toast.makeText(this@ReadListsActivity, "Option 1 SHARE", Toast.LENGTH_SHORT)
                        .show()
//                    implement the share function
                    val sharingIntent = Intent(Intent.ACTION_SEND)
                    sharingIntent.setType("text/plain")
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Lista Canciones: ${list.name}")

                    val stringOfSongs = ArrayList<String>()
                    stringOfSongs.add("Lista  ${list.name} :")
                    var iter = 0
                    for(song in list.songs.values) {
                        iter += 1
                        stringOfSongs.add("${iter} - '${song.title}'")
                    }
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, stringOfSongs.toString())
                    startActivity(Intent.createChooser(sharingIntent, "Share via"))

                    true
                }
                R.id.list_option_2 -> {
                    Toast.makeText(this@ReadListsActivity, "Option 2 DELETE", Toast.LENGTH_SHORT)
                        .show()
                    db.deleteList(list)
                    refreshAll()
                    true
                }
                else ->
                    false
            }
        }
        popup.inflate(R.menu.list_popup_menu)
        popup.setForceShowIcon(true)
        popup.show()
    }


}


