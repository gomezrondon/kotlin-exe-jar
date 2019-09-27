import com.gomezrondon.search.*
import com.mongodb.client.model.Filters.regex
import com.mongodb.client.model.Projections
import org.bson.Document
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

fun main(array: Array<String>) {
    val folders = loadFolders()

    val x:Any = array[0]

    when(x) {
        "0" -> { // index files in folders
            resetRepository()
        }
        "1" -> { // index files in folders
            indexar(folders)
        }
        "2" -> { // search
            val word = array[1]
            parallaleSearch(folders, word)
            combineAllResults()
        }
        "3" -> { // Tokenizer
            createMongoIdexes()
            readTextFile(folders)
            readBinaryfiles(folders)
        }
        "3.1" ->{
            readBinaryfiles(folders)
        }
        "4" -> { // search inside a text file
            val word = array[1]
            searchInFile( word)
        }
        "91" ->{
            val word = "Mongo"
            searchByFileName(word)
        }
        else -> {
            println("Error option equivocada")
        }
    }
}


fun searchByFileName(fileName: String) {
    val collection = mongoCollection()


    val iterDoc = collection.find(regex("path","""\\(?:.*$fileName.*(?!\\))+${'$'}""")).projection(Projections.include("path","doc_id"))//.limit(10) // funcion ojo son las comillas
    iterDoc.forEach { println(it) }
    println("Finish 91 Binarios test...")
}

fun searchInFile( searchWord:String) {
    val toList = """(\w){3,}""".toRegex().findAll(searchWord)?.map { it.value }.map { it.toLowerCase() }.joinToString(" ")

    val collection = mongoCollection()
    //val iterDoc = collection.find(Document("\$text", Document("\$search", "javier")))
    val mongoSearch = "\"$toList\""
    val iterDoc = collection.find(Document("\$text", Document("\$search", mongoSearch))).projection(Projections.include("path","doc_id"))//.limit(10) // funcion ojo son las comillas
    iterDoc.forEach { println(it) }

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
