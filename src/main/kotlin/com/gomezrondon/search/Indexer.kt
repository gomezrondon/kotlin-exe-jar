package com.gomezrondon.search

import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {

    val folders = loadFolders()

    val runnable = {
        indexFiles(folders)
        println("Done Indexing!")
    }

    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable, 1, 60, TimeUnit.SECONDS)

}


fun indexFiles(folders: List<String>) {

    folders.parallelStream().forEach { folder ->

        val index_name = folder.split(File.separator).last().toLowerCase()
        getListOfFilesInFolder(folder, index_name)
        val new_md5 = getMD5(index_name)

        var folderHasChanged = checkFolders(index_name, new_md5)

        println(folderHasChanged.toString() + " "+folder)

        if (folderHasChanged) {
            indexer(folder)
        }
        saveMD5(index_name, new_md5)

    }
}

private fun indexer(folder: String) {
    val index_name = folder.split(File.separator).last().toLowerCase()
    val f_name = "repository" + File.separator + "index_$index_name.txt"

    File(f_name).bufferedWriter().use { out ->
        File(folder).walkTopDown().filter { it.isFile }.forEach {
            val pocLastDate = it.lastModified()
            val linea  = it.name + "," + it.absolutePath + "," + getDateInStr(pocLastDate) + "\n"
            out.write(linea)
        }
    }

}


public fun getDateInStr(s: Long): String? {
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val netDate = Date(s)
        return sdf.format(netDate)
    } catch (e: Exception) {
        return e.toString()
    }
}

public fun getLocalDateTime(s: Long): LocalDateTime {
    val netDate = Date(s)
    val ldt = LocalDateTime.ofInstant(netDate.toInstant(),
            ZoneId.systemDefault())

    return ldt
}

public fun getLocalDateTime(str: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = LocalDateTime.parse(str, formatter)

    return dateTime
}

public fun convertLocalDateToString(ldt: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return ldt.format(formatter)
}