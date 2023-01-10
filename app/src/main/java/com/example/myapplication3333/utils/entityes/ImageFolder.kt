package com.example.myapplication3333.utils.entityes

class ImageFolder {

    var path: String? = null
    var FolderName: String? = null
    var numberOfPics = 0
    var firstPic: String? = null
    var imgPaths = ArrayList<String>()

    fun addpics() {
        numberOfPics++
    }

    fun addpath(path: String) {
        imgPaths.add(path)
    }



}