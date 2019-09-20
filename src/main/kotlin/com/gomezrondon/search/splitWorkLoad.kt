package com.gomezrondon.search

import java.io.File
import java.util.stream.Collectors.groupingBy

fun main() {
    val mapLista = File("index_ca.txt").bufferedReader().useLines {
        it.map { it.split(",").get(1) }  //2nd element
                .filter { it.contains("") } //ojo es un punto (.)
                .map { """^(?:.*?\\){5}""".toRegex().find(it)?.value.toString() }
                .map {
                    val index = it.lastIndexOf(File.separator)
                    it.slice(0..index - 1)
                }
                .sortedBy { it.length }
                .groupingBy { it }.eachCount()
    }


    File("outPut.txt").writeText("")
    var conta = 0
    for ((k,v) in mapLista) {
        conta += v
        println("$v, $k")

        if (v >= 1000) {
            File("outPut.txt").appendText(k+"\n")
        }

    }

    println("total files: $conta") //total files: 67499
}