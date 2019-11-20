import java.io.File


fun main(arg: Array<String>) {
    val textFileList = listOf("pas")
    val listPasFiles = loadFolders(arg.get(0))
            .filter { textFileList.contains(it.extension.toLowerCase()) }

    val listOfWords = arg

    val resultSet: List<String?> = listPasFiles.map { Paquete(it, it.readLines()) }
            .map {
                val absolutePath = it.file.absolutePath
                val toList = it.lines.mapIndexed { index, line ->
                    val first = listOfWords.filter { word -> line.contains(word) }
                            .map { "$index: $line | " + absolutePath }
                            .firstOrNull()

                    first

                }.filter { !it.isNullOrEmpty() }
                toList.toTypedArray()
            }.flatMap { it.asSequence() }.toList()

    val max = resultSet.map { it!!.split("|") }
            .map { it.get(0) }
            .map { it.length }
            .max()?.or(0)

    resultSet.forEach {
        val split = it!!.split("|")
        val parte1 = split.get(0)
        val parte2 = split.get(1)

        println("$parte1 ${max?.let { it1 -> " ".repeat(it1.minus(parte1.length)) }} $parte2")

    }



}


data class Paquete(val file:File, var lines:List<String> )
fun loadFolders(path:String) = File(path).walkTopDown().filter { it.isFile }