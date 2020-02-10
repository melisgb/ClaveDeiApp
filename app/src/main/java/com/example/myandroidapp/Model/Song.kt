package com.example.myandroidapp.Model


class Song {
    var id: Int = 0
    var title: String?= null
    var artist: String?= null
    var lyrics: String?= null
    var tags: String?= null

    constructor(){}

    constructor(id:Int, title:String, artist:String, lyrics:String, tags:String){
        this.id = id
        this.title=title
        this.artist=artist
        this.lyrics=lyrics
        this.tags = tags

    }

}

