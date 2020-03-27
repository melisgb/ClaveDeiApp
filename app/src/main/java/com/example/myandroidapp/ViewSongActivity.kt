package com.example.myandroidapp

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myandroidapp.Model.Song
import com.example.myandroidapp.Model.SongsList
import com.example.myandroidapp.db.DatabaseHelper
import kotlinx.android.synthetic.main.activity_view_song.*


class ViewSongActivity : AppCompatActivity() {

    internal lateinit var db:DatabaseHelper
    var song_id : Int? = null
    var actionMode : ActionMode? = null
    companion object {
        const val EXTRA_SONG_ID = "song_id"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_song)
        title = "CANCION"

        db = DatabaseHelper(this)
        val songId_extra = this.intent.getIntExtra(EXTRA_SONG_ID, 0)
        val current_Song: Song? = db.getSong(songId_extra)
        songLyricsTxtView.setMovementMethod(ScrollingMovementMethod())
        if (songId_extra != 0) {
            song_id = current_Song?.id
            readSongTitleTxtView.setText(current_Song?.title)
//            songTitleTxtView.setText(current_Song?.title)
            songArtistTxtView.setText(current_Song?.artist)
            songLyricsTxtView.setText(current_Song?.lyrics)
            if(current_Song?.tags != ""){
                songTagsTxtView.setText("Tags: "+ current_Song?.tags)
            }
            else songTagsTxtView.setText(current_Song?.tags)
        }
    }

    fun showImpossibleSongDeletion() {
        val layout =  layoutInflater.inflate(R.layout.toast_unable_delete, findViewById(R.id.toast_layout_root))
        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }

    override fun onCreateOptionsMenu( menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.actions_view_song_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        return when (item.itemId) {
            R.id.action_addSongToList -> {
                val summaryLists = db.getSummaryLists()
                var listNamesA = ArrayList<String>()
                for(list in summaryLists) {
                    listNamesA.add(list.name)
                }
//                    if(!listNamesA.contains("Nueva Lista")) {
//                        listNamesA.add("Nueva Lista")
//                    }
//                    else {
//                        listNamesA.add("Nueva Lista"+ Random.nextInt())
//                    }
                val listNames = listNamesA.toArray(emptyArray<String>())

                var selected_ListName = ""
                val builder = AlertDialog.Builder(this@ViewSongActivity)
                builder.setTitle(R.string.choose_list_to_add_song)
                builder.setIcon(R.drawable.add_list_icon)
                builder.setSingleChoiceItems(listNames, -1)  { dialogInterface, i ->
                    selected_ListName = listNames[i]
                    var myListSongs = HashMap<Int, Song>()
                    myListSongs[song_id!!] = db.getSong(song_id!!)!!
                    val listID = db.searchSongsListByName(selected_ListName)

                    if(listID > 0) {
                        //   verify before updating/adding in Favoritos SongsList
                        val oldSongsList = db.getSongsList(listID)
                        if(!oldSongsList.songs.containsKey(song_id!!)) {
                            oldSongsList.songs[song_id!!] = db.getSong(song_id!!)!!
                        }
                        else{
                            print("${song_id} already exists")
                            Toast.makeText(this@ViewSongActivity, "Cancion está en lista", Toast.LENGTH_SHORT).show()
                        }
                        db.updateSongsList(SongsList(listID, selected_ListName, oldSongsList.songs))
                    }
                    else {
                        val songsPerList = HashMap<Int, Song>()
                        songsPerList[song_id!!] = db.getSong(song_id!!)!!
                        db.addSongsList(selected_ListName, myListSongs.values.toList())
                    }

//                        refreshAll()
                    dialogInterface.dismiss()
                }

                builder.setNeutralButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }
                val mDialog = builder.create()
                mDialog.show()
                true
            }

            R.id.action_addSongToFavs -> {
                val favSongsListID = db.searchSongsListByName("Favoritos")
                if(favSongsListID > 0) {
                    //   verify before updating/adding in Favoritos SongsList
                   val oldFavsList = db.getSongsList(favSongsListID)
                   if(!oldFavsList.songs.containsKey(song_id)) {
                            oldFavsList.songs[song_id!!] = db.getSong(song_id!!)!!
                   }
                   else{
                       print("${song_id} already exists")
                       Toast.makeText(this@ViewSongActivity, "Cancion está en lista", Toast.LENGTH_SHORT).show()
                   }
                    db.updateSongsList(SongsList(favSongsListID, "Favoritos", oldFavsList.songs))
                }
                else {
                    val songsPerList = HashMap<Int, Song>()
                    songsPerList[song_id!!] = db.getSong(song_id!!)!!
                    db.addSongsList("Favoritos", songsPerList.values.toList())
                }
                true
            }
            R.id.action_editSong -> {
                val intent = Intent(this@ViewSongActivity, CreateSongActivity::class.java)
                intent.putExtra(CreateSongActivity.EXTRA_SONG_ID, song_id)
                startActivity(intent)
                finish()
                true
            }
            R.id.action_deleteSong -> {
                try{
                    db.deleteSong(db.getSong(song_id!!)!!)
                }
                catch(e: IllegalStateException){
                    showImpossibleSongDeletion()
                }
                val intent = Intent(this@ViewSongActivity, SearchSongsActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> false
        }
    }
}
