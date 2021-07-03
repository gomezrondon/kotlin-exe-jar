import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.*
import kotlin.system.measureTimeMillis


// java -jar kotlin-search.jar C:\temp true/flase <search-words>
fun main(arg: Array<String>) {
    val outFileName = "tempFile.txt"
    val salidaFile="salida.txt"

    val searchDirectory = arg[0] //"D:\\"
    val reBuild:Boolean= arg[1].toBoolean()

/*    val uuid = UUID.randomUUID()
    println(uuid)*/

    createFileWithFileNames(outFileName, searchDirectory,reBuild)
    val list = getListOfFiles(outFileName)

    File(salidaFile).delete()

    val wordList = arg.toMutableList().drop(2)
   // getFilesWithWord(list, wordList, salidaFile)
    generateInvertIndex(list)
}


private fun generateInvertIndex(filesList: List<File>) {

    File("invert-index.txt").delete()

    runBlocking {
        val time = measureTimeMillis {
            val deferred = filesList.map { file ->
                GlobalScope.async {
                    getWordsFromFile(file)
                }
            }

            val sum = deferred.awaitAll()

            sum.forEach {
               // println(it)
                File("invert-index.txt").appendText(  "$it  \n")
            }

        }
        println("total time: $time")
    }
}

fun getWordsFromFile(file: File): indexResultFile {

    val toMutableList = file.readText().split("\n")
        .flatMap { it.split("""\s+""".toRegex()) }
        // .flatMap { it.chunked(3) }
        .flatMap { it.windowed(3) }
        .map { it.lowercase() }
        .filter { !it.isNullOrEmpty() }
        .distinct()
        .toMutableList()
/*        .forEach {
            File(outputFile).appendText(it)
            File(outputFile).appendText( " ")
          //  println(it)
        }*/

    val uuid = UUID.randomUUID().toString().take(8)
    val indexResultFile = indexResultFile(uuid,file.name, file.path, toMutableList)

    return indexResultFile

}


private fun getFilesWithWord(filesList: List<File>, searchList: List<String>, salidaFile: String) {
    runBlocking {
        val time = measureTimeMillis {
            val deferred = filesList.map { file ->
                GlobalScope.async {
                    readFile(file, searchList)
                }
            }
            val sum = deferred.awaitAll().flatten()

            File(salidaFile).appendText("""
                $searchList *** Word searched ***
                *** Programs where the word exist *** 
            """.trimIndent())

            File(salidaFile).appendText(" \n  \n")

            sum.distinctBy { it.path }.forEach {
                File(salidaFile).appendText("+ ${it.path} \n")
            }

            File(salidaFile).appendText(" \n   \n")
            File(salidaFile).appendText("*** what line the word is use in each program ***\n")
            File(salidaFile).appendText(" ")

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

data class indexResultFile(val fileCode: String, val fileName: String, val path:String, val lines: MutableList<String>)
data class SearchResult(val fileName: String, val path:String, val numline: Int, val line: String)
