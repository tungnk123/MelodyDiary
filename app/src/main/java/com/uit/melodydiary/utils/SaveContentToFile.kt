package com.uit.melodydiary.utils

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


fun saveContentListToFile(context: Context, contentList: List<Pair<String, ByteArray>>): String {
    val fileName = "content_${System.currentTimeMillis()}.dat"
    val file = File(context.filesDir, fileName)
    ObjectOutputStream(FileOutputStream(file)).use { it.writeObject(contentList) }
    return file.absolutePath
}

fun loadContentListFromFile(filePath: String): List<Pair<String, ByteArray>> {
    val file = File(filePath)
    ObjectInputStream(FileInputStream(file)).use { return it.readObject() as List<Pair<String, ByteArray>> }
}