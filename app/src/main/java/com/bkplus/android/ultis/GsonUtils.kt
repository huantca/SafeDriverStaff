package com.bkplus.android.ultis

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import timber.log.Timber

object GsonUtils {
    val gson: Gson = GsonBuilder()
        .serializeSpecialFloatingPointValues()
        .create()

    fun <T> saveObject(obj: T): String {
        return gson.toJson(obj)
    }

    fun <T> getObject(json: String?, clazz: Class<T>): T? {
        return try {
            if (json.isNullOrBlank().not()) Gson().fromJson(json, clazz) else null
        } catch (e: JsonSyntaxException) {
            Timber.e(e)
            null
        }
    }

    inline fun <reified T> saveList(list: List<T>, clazz: Class<T>): String {
        return gson.toJson(list.toTypedArray(), TypeToken.getArray(clazz).type)
    }

    fun <T> getList(json: String?, clazz: Class<T>): List<T> {
        return try {
            if (json.isNullOrBlank().not()) {
                Gson().fromJson<Array<T>>(json, TypeToken.getArray(clazz).type).toList()
            } else listOf()
        } catch (e: JsonSyntaxException) {
            Timber.e(e)
            listOf()
        }
    }

    /**
     * using with primitive data type(Long,Integer,String,etc...)
     * [WARNING] NOT USING WITH MODEL CLASS
     */
    inline fun <reified T> fromJsonSafe(json: String?): T? {
        if (json == null) return null
        return try {
            gson.fromJson<T>(json, object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    fun <T> toJson(data: T): String {
        return gson.toJson(data)
    }
}