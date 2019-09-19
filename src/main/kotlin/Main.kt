import com.gomezrondon.search.combineAllResults
import com.gomezrondon.search.indexFiles
import com.gomezrondon.search.loadFolders
import com.gomezrondon.search.parallaleSearch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main(array: Array<String>) {
    val folders = loadFolders()

    when(array[0].toInt()) {
        1 -> { // index files in folders
            indexar(folders)
        }
        2 -> { // search
            val word = array[1]
            parallaleSearch(folders, word)
            combineAllResults()
        }
        else -> {
            println("Error option equivocada")
        }
    }
}

private fun indexar(folders: List<String>) {
    val runnable = {
        indexFiles(folders)
        println("Done Indexing!")
    }
    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable, 1, 60, TimeUnit.SECONDS)
}
