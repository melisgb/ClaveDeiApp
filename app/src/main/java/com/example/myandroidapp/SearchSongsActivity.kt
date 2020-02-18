package com.example.myandroidapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
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


        songs_lstView.setOnItemClickListener{ parent, view, position, id ->
            val intent = Intent(this, CreateSongActivity::class.java)
            intent.putExtra(CreateSongActivity.EXTRA_SONG_ID, id)
            startActivity(intent)

        }
    }

    // implementation of the searchBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search_bar)
        val searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
             override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()
                Toast.makeText(this@SearchSongsActivity, "Looking for $query", Toast.LENGTH_SHORT).show()

                 val listSongs1 = db.searchSongs(query!!)
                 val adapter = ListSongsAdapter(this@SearchSongsActivity , listSongs1)
                 songs_lstView.adapter = adapter


                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                Toast.makeText(this@SearchSongsActivity, "Looking for $newText", Toast.LENGTH_SHORT).show()
                return false
            }
        })
        return true
    }
}
