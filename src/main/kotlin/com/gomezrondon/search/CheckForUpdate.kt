package com.gomezrondon.search

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {
    val runnable = {
        println(">>>>>>>>>>>>>>>>>>>>> Indexing!")
        val folders = loadFolders()

        folders.forEach { folder ->
            val index_name = folder.split("""\""").last().toLowerCase()
            getListOfFilesInFolder(folder, index_name)
            val new_md5 = getMD5(index_name)

            var folderHasChanged = checkFolders(index_name, new_md5)

            println(folderHasChanged.toString() + " "+folder)

            saveMD5(index_name, new_md5)

/*            if (folderHasChanged) {
                saveMD5(index_name, new_md5)
            }*/
        }

        println("Done Indexing!<<<<<<<<<<<<<<<<<<<")
    }

    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable, 1, 30, TimeUnit.SECONDS)
}

fun saveMD5(index_name: String, new_md5: String) {
    val f_name_md5 = "repository" + File.separator + "md5_$index_name.txt"
    File(f_name_md5).writeText(new_md5)
}

fun checkFolders(index_name: String, new_md5: String): Boolean {
    var folderHasChanged = false
    val f_name_md5 = "repository" + File.separator + "md5_$index_name.txt"
    if (File(f_name_md5).exists()) {
        val old_md5 = File(f_name_md5).readLines().joinToString ("")
        if (new_md5 != old_md5) {
            folderHasChanged = true
        }
    }else{
        saveMD5(index_name, new_md5)
        folderHasChanged = true
    }
    return folderHasChanged
}


fun getListOfFilesInFolder(folder: String, index_name:String) {
    val f_name = "repository" + File.separator + "output_$index_name.txt"
    "cmd.exe /c cd $folder & dir /s /A-D".runCommand(timeout = 15, outPutFile = f_name)
}


fun getMD5(index_name:String):String {
    val f_name = "repository" + File.separator + "output_$index_name.txt"

    if (File(f_name).exists()) {
        val md5_hash_str = File(f_name).readLines()
                .map { it.replace("""\s""".toRegex(), "") }
                .filter { !it.contains("bytesfree|bytes".toRegex()) }
                .joinToString("").md5()

       return md5_hash_str
    }
    return ""
}


fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

fun String.runCommand(workingDir: File? = null, timeout:Long, outPutFile:String) {
    if (File(outPutFile).exists()) {
        File(outPutFile).delete()
    }
    val process = ProcessBuilder(*split(" ").toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.appendTo(File(outPutFile)))
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()

    // File(outPutFile).readLines().forEach { println(it) }

    if (!process.waitFor(timeout, TimeUnit.SECONDS)) {
        process.destroy()
        throw RuntimeException("execution timed out: $this")
    }
    if (process.exitValue() != 0) {
        throw RuntimeException("execution failed with code ${process.exitValue()}: $this")
    }


}