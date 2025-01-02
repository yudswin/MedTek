package com.medtek.main.utilties

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.medtek.main.data.local.entities.Habit
import com.medtek.main.data.local.entities.Rituals
import com.medtek.main.data.local.entities.Survey
import com.medtek.main.data.remote.models.survey.ConfigValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    fun fromColor(color: String): String {
        return color
    }

    @TypeConverter
    fun fromConfigValueList(configValues: List<ConfigValue>): String {
        val type = object : TypeToken<List<ConfigValue>>() {}.type
        return gson.toJson(configValues, type)
    }

    @TypeConverter
    fun toConfigValueList(configValuesString: String): List<ConfigValue> {
        val type = object : TypeToken<List<ConfigValue>>() {}.type
        return gson.fromJson(configValuesString, type)
    }

    @TypeConverter
    fun fromSurvey(survey: Survey): String {
        return Gson().toJson(survey)
    }

    @TypeConverter
    fun toSurvey(surveyString: String?): Survey? {
        if (surveyString.isNullOrEmpty()) {
            return null
        }
        val type = object : TypeToken<Survey>() {}.type
        return Gson().fromJson(surveyString, type)
    }

    @TypeConverter
    fun fromHabitList(value: String): List<Habit> {
        val listType = object : TypeToken<List<Habit>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toHabitList(list: List<Habit>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromDailyPlans(value: String): Map<String, List<Habit>> {
        val mapType = object : TypeToken<Map<String, List<Habit>>>() {}.type
        return gson.fromJson(value, mapType)
    }

    @TypeConverter
    fun toDailyPlans(map: Map<String, List<Habit>>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun fromDate(date: Date): String = dateFormat.format(date)

    @TypeConverter
    fun toDate(dateString: String): Date = dateFormat.parse(dateString) ?: Date()
}