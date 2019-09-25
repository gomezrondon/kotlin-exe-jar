import com.gomezrondon.search.*
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

fun main(array: Array<String>) {
    val folders = loadFolders()

    when(array[0].toInt()) {
        0 -> { // index files in folders
            resetRepository()
        }
        1 -> { // index files in folders
            indexar(folders)
        }
        2 -> { // search
            val word = array[1]
            parallaleSearch(folders, word)
            combineAllResults()
        }
        3 -> { // Tokenizer
            readTextFile()
        }
        else -> {
            println("Error option equivocada")
        }
    }
}



fun resetRepository() {
    File("repository").walkTopDown()
            .filter { it.isFile }
            .filter { it.name.startsWith("md5_") || it.extension == "dat" }
            .forEach {
                println("DELETING $it !!!")
                    it.delete()
            }
    println("Finish Reseting...")
}

private fun indexar(folders: List<String>) {
    val runnable = {

        val time = measureTimeMillis {
            println("Start Indexing!")
            indexFiles(folders)
            println("Done Indexing!")
        }
        println("Exe. Time: $time")

    }
    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable, 1, 60, TimeUnit.SECONDS)
}
