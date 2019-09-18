package com.gomezrondon.search

import java.io.File
import kotlin.concurrent.thread

fun main() {


    val word = "javier"
    val  folders = loadFolders()
    //search(word, folders) // run synchronous
    parallaleSearch(folders, word)
    combineAllResults()


}

private fun parallaleSearch(folders: List<String>, word: String) {
    var t_list: MutableList<Thread> = mutableListOf<Thread>()

    folders.forEach {
        val thread = thread {
            val threadName = Thread.currentThread().name
            println("folder $it $threadName")
            search(word, mutableListOf(it))
        }
        t_list.add(thread)
    }

    t_list.parallelStream().forEach { it.join() } // wait for all threads
}

private fun combineAllResults() {
    val regexExec = """.*search_result_.*txt""".toLowerCase().toRegex(RegexOption.MULTILINE)
    val total_lines = File("repository")
            .listFiles()
            .map { it.absolutePath }
            .map { it.toLowerCase() }
            // .flatMap { regexExec1.findAll(it).map { it.value }.toList()} // seleccionamos la porcion del texto que vamos a revisar.
            .filter { regexExec.containsMatchIn(it) }
            .flatMap { File(it).readLines() }
            .filter { !it.startsWith("Num.") }
            .filter { it.isNotEmpty() }

    total_lines.forEach { println(it) }
    print("\n\nNum. Total ${total_lines.size}")
}

public fun search(word: String, folders:List<String>) {

    val regexExec = """.*$word.*""".toLowerCase().toRegex(RegexOption.MULTILINE)

    var search_result = mutableListOf<String>()

    folders.parallelStream().forEach { folder ->
        val index_name = folder.split("""\""").last().toLowerCase()
        val f_name = "repository" + File.separator + "index_$index_name.txt"

        if (File(f_name).exists()) {
            search_result.addAll(searchInFiles(f_name, regexExec))

            val outPut = "repository" + File.separator + "search_result_$index_name.txt"
            write_search_results(search_result, outPut)
            search_result.clear()
        }else{
            println("folder: $f_name does not Exist!")
        }

    }


}

private fun write_search_results(search_result: MutableList<String>, outPut: String) {
    val size = search_result.size

    var max = 30

    File(outPut).bufferedWriter().use { out ->

        search_result.forEach {
            val arre = it.split(",")
            var repeat = (max + 15) - arre.get(0).length
            if (repeat < 5) {
                repeat = 30
            }
            val line = arre.get(0) + " ".repeat(repeat) + arre.get(1) + "\n"
            out.write(line)

        }
        out.write("\n\nNum. Results: $size")
    }
}

fun loadFolders() = File("repository/folders.txt").readLines().filter { !it.startsWith("--") }

private fun searchInFiles(f_name: String, regexExec: Regex): List<String> {
    val regexExec1 ="""^(.*?),""".toLowerCase().toRegex(RegexOption.MULTILINE) // hasta la primera coma
    return  File(f_name).readLines()
            .map { it.toLowerCase() }
            .filter {
                regexExec.containsMatchIn(regexExec1.find(it)?.value.toString())
            }
}

