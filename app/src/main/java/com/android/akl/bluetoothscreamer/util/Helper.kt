package com.android.akl.bluetoothscreamer.util

/**
 * Created by Mohamed Akl on 2/1/2021.
 */
class Helper {
    companion object{
        fun charSequenceToString(list: List<CharSequence>): List<String>{
            val stringList: MutableList<String> = mutableListOf()
            for (item in list){
                stringList.add(item as String)
            }
            return stringList
        }
    }
}