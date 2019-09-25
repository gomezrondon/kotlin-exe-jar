package com.gomezrondon.search

import java.io.File

data class Paquete(val file:File, var lines:List<String> )

fun readTextFile(folders: List<String>) {

    val textFileList = listOf<String>("txt","sql","java","py","bat","csv")

    folders.parallelStream().forEach { folder ->

        File(folder).walkTopDown()
                .filter { textFileList.contains(it.extension) }
                .take(400) // just one for testing
                .map { Paquete(it, it.readLines() )}
                .map {
                    val wordList =it.lines.flatMap { """(\w){3,}""".toRegex().findAll(it)?.map { it.value }.map { it.toLowerCase() }.toList() }
                    it.lines = wordList
                    it
                }
                .forEach {
                    val name = it.file.name.substringBefore(".")
                    val f_name = name + ".dat"
                    wirteToFile(f_name, it)
                }

    }




    println("Finish 90 test...")
}

private fun wirteToFile(f_name: String, it: Paquete) {
    File("repository" + File.separator + "words" + File.separator + f_name).bufferedWriter().use { out ->

        out.write(it.file.absolutePath+"\n")
        out.write(it.file.absolutePath.toString().md5()+"\n")

        var countLetters = 0
        var line = StringBuilder("")

        it.lines.forEach { word ->
            if (countLetters > 1500) {
                line.append(word + ",")
                out.write(line.toString() + "\n")
                line.clear()
                countLetters = 0
            } else {
                line.append(word + ",")
                // out.write(word + ",")
            }
            countLetters = line.length
        }
        if (line.isNotEmpty()) {
            out.write(line.toString())
        }
    }
}


