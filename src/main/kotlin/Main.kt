import com.gomezrondon.search.*
import java.io.File
import java.lang.ProcessBuilder.Redirect
import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


fun main() {
    val word = "role|jpg"
    //println(word)
    //search(word)

    val runnable = {
        println(">>>>>>>>>>>>>>>>>>>>> Indexing!")
        val folders = loadFolders()
        getListOfFilesInFolder(folders)
        getMD5(folders)
        println("Done Indexing!<<<<<<<<<<<<<<<<<<<")
    }

    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable, 1, 30, TimeUnit.SECONDS)

}

fun getListOfFilesInFolder(folders: List<String>) {
    folders.parallelStream().forEach { folder ->
        val index_name = folder.split("""\""").last().toLowerCase()
        val f_name = "repository" + File.separator + "output_$index_name.txt"
        "cmd.exe /c cd $folder & dir /s /A-D".runCommand(timeout = 15, outPutFile = f_name)
    }
}


fun getMD5(folders: List<String>) {
    folders.parallelStream().forEach { folder ->
        val index_name = folder.split("""\""").last().toLowerCase()
        val f_name = "repository" + File.separator + "output_$index_name.txt"
        val f_name_clean = "repository" + File.separator + "md5_$index_name.txt"
        if (File(f_name).exists()) {
            val md5_hash_str = File(f_name).readLines()
                    .map { it.replace("""\s""".toRegex(), "") }
                    .filter { !it.contains("bytesfree|bytes".toRegex()) }
                    .joinToString("").md5()
            File(f_name_clean).writeText(md5_hash_str)
        }
    }
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
            .redirectOutput(Redirect.appendTo(File(outPutFile)))
            .redirectError(Redirect.INHERIT)
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