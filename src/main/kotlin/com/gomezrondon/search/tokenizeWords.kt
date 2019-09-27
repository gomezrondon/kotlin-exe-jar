package com.gomezrondon.search

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.*
import org.bson.Document
import java.io.File
import java.util.ArrayList

data class Paquete(val file:File, var lines:List<String> )


data class DataFile(val id:String, val type:String = "data-file", val path:String="", val lines:List<String>){

    fun getMongoDocument(): Document {
        val document = Document("doc_id", id)
                .append("type", type)
                .append("path", path)
                .append("doc_texto", lines)

        return document
    }

    fun getIdDocument(): Document {
        return Document("doc_id", id)
    }

    fun getReplaceLinesDocument():Document {
        return Document("\$set", Document("doc_texto", lines) )
    }
}




fun readBinaryfiles(folders: List<String>) {
    val collection = mongoCollection()

    val noSearchList = dontSearchList()
    val textFileList = listOf("png", "jpg","jpeg","mp3")

    collection.deleteMany(Filters.eq("type", "binary-file"))

    val bulkWriteOptions = BulkWriteOptions().ordered(false)


    folders.parallelStream().forEach { folder ->
        val documents = ArrayList<WriteModel<Document>>()
        File(folder).walkTopDown()
                .filter { textFileList.contains(it.extension.toLowerCase()) }
                .filter{filterBlackListPath(noSearchList, it) }
                .forEach {
                    // build the document
                    val dataFile = DataFile(id = it.absolutePath.toString().md5()
                            ,type = "binary-file"
                            ,path = it.absolutePath.toLowerCase()
                            ,lines = listOf() )

                    //collection.insertOne(dataFile.getMongoDocument())
                    documents.add(InsertOneModel(dataFile.getMongoDocument()))
                   // println("Inserting record: ${dataFile.path}  ${dataFile.id}")
                }

        if (documents.isNotEmpty()) {
            collection.bulkWrite(documents, bulkWriteOptions)
        }

    }


    println("Finish 90 test...")
}

fun readTextFile(folders: List<String>) {

    val collection = mongoCollection()

    val noSearchList = dontSearchList()
    val textFileList = listOf("txt","sql","java","py","bat","csv","kt","kts")

    folders.parallelStream().forEach { folder ->

         File(folder).walkTopDown()
                .filter { textFileList.contains(it.extension) }
                //.take(400) // just one for testing
                //.filter { !noSearchList.contains(getPathByLevel(5, it.absolutePath)) }
                .filter{filterBlackListPath(noSearchList, it) }
                .map { Paquete(it, it.readLines() )}
                .map {
                    val wordList =it.lines.flatMap { """(\w){3,}""".toRegex().findAll(it)?.map { it.value }.map { it.toLowerCase() }.toList() }
                    it.lines = wordList
                    it
                }
                .forEach {
                    val name = it.file.absolutePath.md5()
                    val f_name = name + ".dat"
                    //wirteToFile(f_name, it)
                    writeToMongo(f_name, it, collection)
                }

    }
    println("Finish 90 test...")
}

fun mongoCollection(): MongoCollection<Document> {
    val conn = MongoDBConnection()
    val database = conn.database
    val collection = database.getCollection("documentx")
    return collection
}

fun createMongoIdexes() {
    val collection = mongoCollection()

    val listIndexes = collection.listIndexes()

    var indexExist = false
    listIndexes.forEach {
        for ((k,v) in it) {
           // println("K: $k value: $v")
            if (k == "name" && v == "doc_texto_text") {
                indexExist= true
                println("Index: $v  Exist!")
            }
        }
    }

    if (!indexExist) {
        collection.createIndex(Indexes.text("doc_texto"))
        println("Index: doc_texto_text Created!")
    }


}

fun filterBlackListPath(noSearchList: List<String>, it: File): Boolean {
    var exist = true
    for (dir: String in noSearchList) {
        if (it.absolutePath.startsWith(dir)) {
            exist = false
        }
    }
    return exist
}


private fun writeToMongo(f_name: String, it: Paquete, collection: MongoCollection<Document>) {
    var lineas = mutableListOf<String>()
    var countLetters = 0
    var line = StringBuilder("")

    it.lines.forEach { word ->
        if (countLetters > 500) {
            line.append(word + " ")
            //out.write(line.toString() + "\n")
            lineas.add(line.toString())
            line.clear()
            countLetters = 0
        } else {
            line.append(word + " ")
        }
        countLetters = line.length
    }
    if (line.isNotEmpty()) {
       // out.write(line.toString())
        lineas.add(line.toString())
    }

    // build the document
    val dataFile = DataFile(id = it.file.absolutePath.toString().md5()
            ,path = it.file.absolutePath.toLowerCase()
            ,lines = lineas )


    val iterDoc = collection.findOneAndUpdate(dataFile.getIdDocument(), dataFile.getReplaceLinesDocument())

    if (iterDoc == null ) {
        //insert the document
        collection.insertOne(dataFile.getMongoDocument())
        println("Inserting record: ${dataFile.path}  ${dataFile.id}")
    }else{
        println("Updating record: ${dataFile.path}  ${dataFile.id}")
    }


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


