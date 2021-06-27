import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

    arg.drop(2).forEach { word ->
        getFilesWithWord(list, word, salidaFile)
        //     countFileLinesCoroutines(list, word, salidaFile)  // each word to search mObjGaaData
    }
}





private fun getFilesWithWord(toList: List<File>, word: String, salidaFile: String) {
    val prefix = "C:\\Users\\JGomez\\source\\Workspaces\\ApplicationsGAA"
    runBlocking {

        val time = measureTimeMillis {
            val deferred = toList.map { file ->
                GlobalScope.async {
                    readFile(file, word)
                }
            }
            val sum = deferred.awaitAll().flatten()

            //   File(salidaFile).delete()

            File(salidaFile).appendText("$word *** Word searched ***\n")
            File(salidaFile).appendText("*** Programs where the word exist ***\n")
            sum.distinctBy { it.path }.forEach {
                val relativePath = it.path.removePrefix(prefix)
                File(salidaFile).appendText("+ ${relativePath} \n")
                //   println("${it.numline}:   ${it.path} >> ${it.line}")
            }

            File(salidaFile).appendText("*** what line the word is use in each program ***\n")
            sum.forEach {
                val relativePath = it.path.removePrefix(prefix)
                File(salidaFile).appendText("${it.numline}:   ${relativePath} >> ${it.line} \n")
                //   println("${it.numline}:   ${it.path} >> ${it.line}")
            }
            File(salidaFile).appendText("\n")

        }
        println("total time: $time")
    }
}



fun readFile(file: File, word: String): List<SearchResult> {
    //  println("File ${file.name} total lines: $size")
    return file.readText().split("\n")
        .mapIndexed { index, s -> Pair(index + 1,s) }
        .filter { it.second.lowercase().contains(word.lowercase()) }
        .map { SearchResult(file.name, file.path,numline =  it.first, line = it.second) }

}

data class SearchResult(val fileName: String, val path:String, val numline: Int, val line: String)
