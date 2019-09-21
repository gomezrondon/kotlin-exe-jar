package com.gomezrondon.search

import java.io.File
import java.util.stream.Collectors.groupingBy

fun main() {

     loadFolders().forEach{folder ->
         val index_name = folder.split("""\""").last().toLowerCase()
         val f_name = "repository" + File.separator + "index_$index_name.txt"

         val mapLista = getMapOfPaths(f_name)
         val outPut="repository/outPut.txt"
         //File(outPut).writeText("") // reset file

         var conta = filterBigFolders(max=1000, mapLista = mapLista, outPut = outPut)

         println("total files: $conta") //total files: 67499

     }

}

private fun filterBigFolders(max:Int = 1000, mapLista: Map<String, Int>, outPut: String): Int {
    var conta = 0
    for ((k, v) in mapLista) {
        conta += v
        println("$v, $k")

        if (v >= max) {
            File(outPut).appendText(k + "\n")
        }

    }
    return conta
}

private fun getMapOfPaths(f_name: String): Map<String, Int> {
    return File(f_name).bufferedReader().useLines {
        it.map { it.split(",").get(1) }  //2nd element
                .filter { it.contains("") } //ojo es un punto (.)
                .map { getPathByLevel(level = 6, path = it) }
/*                .map {
                    val index = it.lastIndexOf(File.separator)
                    it.slice(0..index - 1)
                }*/
                .sortedBy { it.length }
                .groupingBy { it }.eachCount()
    }
}

public fun getPathByLevel(level:Int = 6 ,path: String) = """^(?:.*?\\){$level}""".toRegex().find(path)?.value.toString() //ojo es un punto (.)