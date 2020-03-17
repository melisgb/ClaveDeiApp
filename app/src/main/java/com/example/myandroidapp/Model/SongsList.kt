package com.example.myandroidapp.Model


import java.io.Serializable

class SongsList  : Serializable {
    var id: Int
    var name: String
    var songs: HashMap<Int, Song>


    constructor(id:Int, name:String, songs: HashMap<Int, Song>){
        this.id = id
        this.name = name
        this.songs = songs

    }

}
