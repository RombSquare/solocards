package com.rombsquare.solocards.features.menu_feature.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

fun writeJsonToFile(context: Context, uri: Uri, jsonString: String): Boolean {
    return try {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(jsonString.toByteArray(Charsets.UTF_8))
        }
        true
    } catch (e: Exception) {
        Log.e("SolocardsTest", "Error with writing json file: $e")
        false
    }
}

fun readJsonFromFile(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val stringBuilder = StringBuilder()

        BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).use { reader ->
            var line: String? = reader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = reader.readLine()
            }
        }
        stringBuilder.toString()
    } catch(_: Exception) {
        null
    }
}

fun shareTextAsJson(context: Context, jsonString: String, name: String) {
    val cacheDir = File(context.cacheDir, "data_to_share").apply { mkdirs() }
    val file = File(cacheDir, "$name.json")
    file.writeText(jsonString)

    val authority = "${context.packageName}.fileprovider"
    val fileUri = FileProvider.getUriForFile(context, authority, file)

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/json"
        putExtra(Intent.EXTRA_STREAM, fileUri)
        putExtra(Intent.EXTRA_SUBJECT, "Shared: $name")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    val chooser = Intent.createChooser(intent, "Share via")
    chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(chooser)
}