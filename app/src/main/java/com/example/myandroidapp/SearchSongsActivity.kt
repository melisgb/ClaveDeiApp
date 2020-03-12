package com.example.myandroidapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myandroidapp.Adapter.ListSongsAdapter
import com.example.myandroidapp.Model.Song
import com.example.myandroidapp.db.DatabaseHelper
import kotlinx.android.synthetic.main.activity_search_songs.*
import kotlinx.android.synthetic.main.list_elem_search_songs.*


class SearchSongsActivity : AppCompatActivity() {

    internal lateinit var db: DatabaseHelper
    var s_keyword : String? = null
    var selected_Set = HashSet<Long>()
    var actionMode : ActionMode? = null
    var adapter : ListSongsAdapter? = null

//    implementation of Songs Action mode - later implement it as class and interface.
    private val actionModeCallback = object : ActionMode.Callback {
        // Called when the action mode is created
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.actions_song_menu, menu)
            mode.title = "Choose action"
            return true
        }
        // Called each time the action mode is shown. Always called after onCreateActionMode.
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_addToList -> {
                    Toast.makeText(this@SearchSongsActivity, "Add To List", Toast.LENGTH_SHORT).show()

                    Toast.makeText(this@SearchSongsActivity, "Songs list $selected_Set", Toast.LENGTH_SHORT).show()
                    var myListSongs = ArrayList<Song>()
                    for(songID in selected_Set){
                        myListSongs.add(db.getSong(songID)!!)
                    }

//                    val adapter = ListSongsAdapter(this, myListSongs)

                    val intent = Intent(this@SearchSongsActivity, ReadMyListActivity::class.java)
                    startActivity(intent)


                    mode.finish() // Action picked, so close the CAB
                    true
                }
                R.id.action_addToFavs -> {
                    Toast.makeText(this@SearchSongsActivity, "Add Favs", Toast.LENGTH_SHORT).show()
                    mode.finish() // Action picked, so close the CAB
                    true
                }
                R.id.action_deleteSong -> {
                    Toast.makeText(this@SearchSongsActivity, "Deleting $selected_Set", Toast.LENGTH_SHORT).show()
                    for(song in selected_Set){
                        db.deleteSong(db.getSong(song)!!)
                    }
                    refreshAll()
                    adapter?.listSong = db.getSongs()
                    adapter?.notifyDataSetChanged()
//                    val intent = Intent(this@SearchSongsActivity, SearchSongsActivity::class.java)
//                    startActivity(intent)
                    mode.finish() // Action picked, so close the CAB
                    true
                }
                else ->
                    false
            }
        }

        // Called when the user exits the action mode
        override fun onDestroyActionMode(mode: ActionMode){
            actionMode = null
        }
    }

    companion object {
        const val EXTRA_KEYWORD = "s_keyword"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_songs)

        db = DatabaseHelper(this)
//        searchSpecificSongs()

        val listSong = db.getSongs()
        adapter = ListSongsAdapter(this, listSong)
        songs_lstView.adapter = adapter


        songs_lstView.setOnItemClickListener { parent, view, position, id ->
            if(selected_Set.isEmpty()) {
                val intent = Intent(this, CreateSongActivity::class.java)
                intent.putExtra(CreateSongActivity.EXTRA_SONG_ID, id)
                startActivity(intent)
            }
            else if(selected_Set.contains(id)){
                selected_Set.remove(id)
                val checkbox = view.findViewById<CheckBox>(songCheckBox.id)
                checkbox.isChecked = false
                checkbox.visibility = View.INVISIBLE
            }
            else if(!selected_Set.contains(id)){
                selected_Set.add(id)
                val checkbox = view.findViewById<CheckBox>(songCheckBox.id)
                checkbox.isChecked = true
                checkbox.visibility = View.VISIBLE
            }


        }
        songs_lstView.setOnItemLongClickListener { parent, view, position, id ->
            Toast.makeText(this@SearchSongsActivity, "Long click on $position", Toast.LENGTH_SHORT).show()

            if(selected_Set.contains(id)) {
                selected_Set.remove(id)
                val checkbox = view.findViewById<CheckBox>(songCheckBox.id)
                checkbox.isChecked = false
                checkbox.visibility = View.INVISIBLE
            }
            else if(!selected_Set.contains(id)) {
                selected_Set.add(id)
                val checkbox = view.findViewById<CheckBox>(songCheckBox.id)
                checkbox.isChecked = true
                checkbox.visibility = View.VISIBLE
            }

            //agregar menu de songs
            when (actionMode) {
                null -> {
                    // Start the CAB using the ActionMode.Callback defined above
                    actionMode = this@SearchSongsActivity.startActionMode(actionModeCallback)
//                    view.isSelected = true
                    true
                }
                else -> true
            }
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

        val keyword_extra: String? = this.intent.getStringExtra(EXTRA_KEYWORD)

        if (!keyword_extra.isNullOrEmpty()) {
            searchView.setQuery(keyword_extra, true)
        }

        return true
    }

    fun refreshAll(){
        selected_Set.clear()
    }
}


