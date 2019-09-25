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
            readTextFile(folders)
        }
        90 -> { // Testing
            val wordFileList = loadWordFiles()
            val searchWord = "javier gomez"
            searchInFile(wordFileList, searchWord)
        }
        else -> {
            println("Error option equivocada")
        }
    }
}

fun searchInFile(wordFileList: List<File>, searchWord:String) {

    val toList = """(\w){3,}""".toRegex().findAll(searchWord)?.map { it.value }.map { it.toLowerCase() }.joinToString(".")

    val regexExec ="""$toList""".toLowerCase().toRegex(RegexOption.MULTILINE)

    wordFileList.parallelStream().filter { it.isFile }.forEach { file ->

        file.walkTopDown()
                .filter { it.isFile }
                .map { Paquete(it, it.readLines() )}
                .filter {
                    it.lines.filter { line -> regexExec.containsMatchIn(line) }.size > 0

                }
                .forEach { println(it.lines.get(0)) }

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
