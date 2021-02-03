package com.android.akl.bluetoothscreamer.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.android.akl.bluetoothscreamer.util.Constants.Companion.DEFAULT_END_TIME
import com.android.akl.bluetoothscreamer.util.Constants.Companion.DEFAULT_START_TIME
import com.android.akl.bluetoothscreamer.util.Constants.Companion.END_TIME
import com.android.akl.bluetoothscreamer.util.Constants.Companion.START_TIME
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

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

        @SuppressLint("SimpleDateFormat")
        fun isOnOffHours(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(Constants.DISPAGER_DEVICES, AppCompatActivity.MODE_PRIVATE)
            val startTime = sharedPreferences.getString(START_TIME, DEFAULT_START_TIME)
            val endTime = sharedPreferences.getString(END_TIME, DEFAULT_END_TIME)
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val current = LocalTime.now().format(formatter)
            val startTimeLDT = LocalTime.parse(startTime, formatter)
            val endTimeLDT = LocalTime.parse(endTime, formatter)
            val currentTimeLDT = LocalTime.parse(current, formatter)
            return (!Duration.between(startTimeLDT, currentTimeLDT).isNegative &&
                    !Duration.between(currentTimeLDT, endTimeLDT).isNegative)
        }

        fun getAppropriateString(int: Int): String{
            return if(int < 10)
                "0$int"
            else
                "$int"
        }

        fun getHourMin(string: String): List<Int>{
            return listOf(string.split(":")[0].toInt(), string.split(":")[1].toInt())
        }
    }
}