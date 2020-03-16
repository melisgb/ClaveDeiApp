package com.example.myandroidapp.Model


import java.io.Serializable

class SongsList  : Serializable {
    var id: Int = 0
    var name: String?= null
    var songs: ArrayList<Song>?= null

    constructor()

    constructor(id:Int, name:String, songs:ArrayList<Song>){
        this.id = id
        this.name=name
        this.songs=ArrayList<Song>()

    }

}
