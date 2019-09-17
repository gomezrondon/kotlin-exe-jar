package com.gomezrondon.search

import java.io.File

fun main() {
    val word = ".sql"
    search(word)
}

public fun search(word: String) {
    val folders = loadFolders()
    val regexExec = """.*$word.*""".toLowerCase().toRegex(RegexOption.MULTILINE)

    var search_result = mutableListOf<String>()

    folders.forEachIndexed { i, folder ->
        val f_name = "repository" + File.separator + "index_$i.txt"
        search_result.addAll(searchInFiles(f_name, regexExec))
    }

    val size = search_result.size

    var max = 30
    val outPut = "repository" + File.separator + "search_result.txt"
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
    return File(f_name).readLines()
            .map { it.toLowerCase() }
            .flatMap { regexExec1.findAll(it).map { it.value }.toList()} // seleccionamos la porcion del texto que vamos a revisar.
            .filter { regexExec.containsMatchIn(it) }
}
