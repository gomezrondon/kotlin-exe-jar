import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.runBlocking
import java.io.File
import java.sql.DriverManager.println
import kotlin.system.measureTimeMillis



// java -jar kotlin-search.jar C:\temp true/flase <search-words>
fun main(arg: Array<String>) {
    val outFileName = "tempFile.txt"
    val salidaFile="salida.txt"

    val searchDirectory = arg[0] //"D:\\"
    val reBuild:Boolean= arg[1].toBoolean()

    createFileWithFileNames(outFileName, searchDirectory,reBuild)
    val list = getListOfFiles(outFileName)

    File(salidaFile).delete()

    val wordList = arg.toMutableList().drop(2)
    getFilesWithWord(list, wordList, salidaFile)

}

private fun getFilesWithWord(toList: List<File>, searchList: List<String>, salidaFile: String) {
    runBlocking {
        val time = measureTimeMillis {
            val deferred = toList.map { file ->
                GlobalScope.async {
                    readFile(file, searchList)
                }
            }
            val sum = deferred.awaitAll().flatten()

            File(salidaFile).appendText("$searchList *** Word searched ***\n")
            File(salidaFile).appendText("*** Programs where the word exist ***\n")
            File(salidaFile).appendText("")

            sum.distinctBy { it.path }.forEach {
                File(salidaFile).appendText("+ ${it.path} \n")
            }

            File(salidaFile).appendText("")
            File(salidaFile).appendText("*** what line the word is use in each program ***\n")
            File(salidaFile).appendText("")

            sum.forEach {
                File(salidaFile).appendText("${it.numline}:   ${it.path} >> ${it.line} \n")
                //   println("${it.numline}:   ${it.path} >> ${it.line}")
            }
            File(salidaFile).appendText("\n")

        }
        println("total time: $time")
    }
}

fun readFile(file: File, searchList: List<String>): List<SearchResult> {
    return file.readText().split("\n")
        .mapIndexed { index, s -> Pair(index + 1,s) }
        .filter {  containsAll(it.second,searchList ) }
        .map { SearchResult(file.name, file.path,numline =  it.first, line = it.second.toString()) }
}

private fun containsAll(lineList: String, searchList: List<String>): Boolean {
    var contains = false

    for (word: String in searchList) {
        contains = lineList.contains(word)

        if (!contains) {
            break
        }
    }

    return contains
}

data class SearchResult(val fileName: String, val path:String, val numline: Int, val line: String)
