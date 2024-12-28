package com.medtek.main.utilties

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.medtek.main.data.local.entities.DailyRitual
import com.medtek.main.data.local.entities.Survey
import com.medtek.main.data.local.entities.Rituals

class Converters {
    private val gson = Gson()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromDailyRitualList(value: String): List<DailyRitual> {
        val listType = object : TypeToken<List<DailyRitual>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toDailyRitualList(list: List<DailyRitual>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromSurveyList(value: String): List<Survey> {
        val listType = object : TypeToken<List<Survey>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toSurveyList(list: List<Survey>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromRituals(value: String): Rituals {
        return gson.fromJson(value, Rituals::class.java)
    }

    @TypeConverter
    fun toRituals(rituals: Rituals): String {
        return gson.toJson(rituals)
    }


    @TypeConverter
    fun fromDate(date: Date): String = dateFormat.format(date)

    @TypeConverter
    fun toDate(dateString: String): Date = dateFormat.parse(dateString) ?: Date()
}