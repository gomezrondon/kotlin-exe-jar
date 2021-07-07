package com.gomezrondon.search

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@SpringBootApplication
class SearchApplication(val repo:IndexResultFileRepository, val CodePathRepo:CodePathRepository, val invertIndexRepo: InvertIndexRecordRepository) : CommandLineRunner {

	override fun run(vararg arg: String?) {
		val outFileName = "tempFile.txt"
		val salidaFile="salida.txt"

		val searchDirectory = arg[0] //"D:\\"
		val reBuild:Boolean= arg[1].toBoolean()

		if (searchDirectory != null) {
			createFileWithFileNames(outFileName, searchDirectory,reBuild)
		}
		val list = getListOfFiles(outFileName)

		File(salidaFile).delete()

		val invertIndex = generateInvertIndex(list)
		savePathPathRecord(invertIndex)
		val map = generateInvertIndexMap(invertIndex)
		saveInvertIndex(map)

		CodePathRepo.findAll().forEach {
			println(it)
		}

		invertIndexRepo.findAll().forEach {
			println(it)
		}
	}


	private fun generateInvertIndexMap(list: List<IndexResultFile>): ConcurrentHashMap<String, MutableList<String>> {
		val map = ConcurrentHashMap<String, MutableList<String>>()

		list.forEach {
			val fileCode = it.fileCode
			val lines = it.lines.toList()
			lines.forEach { word ->
				if (map.get(word) == null) {
					map[word] = mutableListOf(fileCode)
				} else {
					map.get(word)?.add(fileCode)
				}
			}
		}

		return map
	}

	private fun savePathPathRecord(list: List<IndexResultFile>)  {
		list.forEach {
			CodePathRepo.save(CodePath(it.fileCode,it.path))
		}
	}

	private fun saveInvertIndex(map: ConcurrentHashMap<String, MutableList<String>>) {
		map.forEach { t, u ->
			//	File("invert-index.txt").appendText(  "$t | $u  \n")
			invertIndexRepo.save(InvertIndexRecord(null,t,u.toList()))
			}
	}





	private fun generateInvertIndex(filesList: List<File> ): List<IndexResultFile> {

		File("invert-index.txt").delete()
		File("file-code.txt").delete()

		val list1 = runBlocking {
			val deferred = filesList.map { file ->
				GlobalScope.async {
					getWordsFromFile(file)
				}
			}

			val list = deferred.awaitAll()

			CodePathRepo.findAll().forEach {
				println(it)
			}

			return@runBlocking list
		}

		return list1
	}// generateInvertIndex

}

@Document
data class CodePath(@Id val fileCode: String, val path:String )

fun main(args: Array<String>) {
	runApplication<SearchApplication>(*args)
}

interface IndexResultFileRepository: MongoRepository<IndexResultFile, String>{
}

interface CodePathRepository: MongoRepository<CodePath, String>{
}

interface InvertIndexRecordRepository: MongoRepository< InvertIndexRecord, String>{
}

fun getWordsFromFile(file: File): IndexResultFile {

	val toMutableList = file.readText().split("\n")
		.flatMap { it.split("""\s+""".toRegex()) }
		// .flatMap { it.chunked(3) }
		.flatMap { it.windowed(3) }
		.map { it.lowercase() }
		.filter { !it.isNullOrEmpty() }
		.distinct()
		.toMutableList()

	val uuid = UUID.randomUUID().toString().take(8)

	return IndexResultFile(uuid, file.name, file.path, toMutableList)

}

@Document
data class InvertIndexRecord(@Id val Id: String?, val word: String, val codeList:List<String> )

@Document
data class IndexResultFile(@Id val fileCode: String, val fileName: String, val path:String, val lines: MutableList<String>)
