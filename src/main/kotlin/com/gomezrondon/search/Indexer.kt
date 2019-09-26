package com.gomezrondon.search

import com.gomezrondon.search.Indexer.indexexReactive
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

fun loadFolders() = File("repository${File.separator}folders.txt").readLines().filter { !it.startsWith("--") }
fun dontSearchList() = File("repository${File.separator}outPut.txt").readLines().ifEmpty { mutableListOf() }
fun loadWordFiles() = File("repository${File.separator}words").listFiles().asList()


fun indexFiles(folders: List<String>) {

    folders.parallelStream().forEach { folder ->
        val folderName = getFolderName(folder)
        getListOfFilesInFolder(folder, folderName)
        val new_md5 = getMD5(folderName)

        var folderHasChanged = checkFolders(folderName, new_md5)

        println(folderHasChanged.toString() + " "+folder)

        if (folderHasChanged) {
            indexexReactive(folder) //MUY MUY LENTO!!
        }
        saveMD5(folderName, new_md5)
    }



}

private fun indexer(folder: String) {
    val noSearchList = dontSearchList()

    val index_name = getFolderName(folder)
    val f_name = "repository" + File.separator + "index_$index_name.txt"

    File(f_name).bufferedWriter().use { out ->
        File(folder).walkTopDown().filter { it.isFile }
               // .filter {  !noSearchList.contains(getPathByLevel(level = 6, path = it.absolutePath)) }
                .filter{filterBlackListPath(noSearchList, it) }
                .forEach {
            val pocLastDate = it.lastModified()
            val linea  = it.name + "," + it.absolutePath + "," + getDateInStr(pocLastDate) + "\n"
            out.write(linea)
        }
    }

}

public fun getFolderName(folder: String) = folder.split(File.separator).last().toLowerCase()


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



public fun generateIndexFile_2(folder: String, block: List<List<String>>){
    val folderName = getFolderName(folder)
    val f_name = "repository" + File.separator + "index_" + folderName + ".txt"

    File(f_name).bufferedWriter().use { out ->
        block.forEach { line ->
            val regex = "Directory of "
            val split = line.stream().findFirst().get().split(regex.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val directory = split[1]

            val noSearchList = dontSearchList()
            val isBlackListed = filterBlackListPath(noSearchList, File(directory))

            line.stream()
                        .filter({isBlackListed})
                        .skip(1)
                        .map({ it.substring(39) + "," + it.substring(0, 20) })
                        .map({ x -> x.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray() })
                        .map({ arre -> arre[0] + "," + directory + File.separator + arre[0] + "," + arre[1] + "\n" })
                        .forEach { out.write(it) }

        } // line
    }// write out

}
