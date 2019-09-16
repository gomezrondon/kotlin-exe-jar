import java.io.File

//C:\temp

fun main() {

    ///------------ parte 1 indexa
    val folder = """C:\Users\jrgm\Documents"""
    //val folder = "C:\\temp"
    "C:\\CA"
    //C:\Users\jrgm\Documents
    val indexFile = File("index.txt")
    if (indexFile.exists()){
        indexFile.delete()
    }

    File("index.txt").bufferedWriter().use { out ->
        File(folder).walkTopDown().filter { it.isFile }.forEach { out.write(it.name +","+ it.absolutePath + "\n") }
    }
  ///------------ parte 2
    val regexExec ="""^.*angu.*""".toRegex(RegexOption.MULTILINE)

    val lines = File("index.txt").readLines()

    val search_result = lines.flatMap { regexExec.findAll(it).map { it.value }.toList() }
            .map { it.replace("* ", "") }

    val size = search_result.size

//    var max = search_result.map { it.length }.max() ?: 0
    var max = 30

    if (File("search_result.txt").exists()){
        File("search_result.txt").delete()
    }

    File("search_result.txt").bufferedWriter().use { out ->

        search_result.forEach {
            val arre = it.split(",")
            val repeat= (max + 15) - arre.get(0).length
            val line = arre.get(0) + " ".repeat(repeat) + arre.get(1) + "\n"
            out.write(line)

        }
        out.write("Num. Results: $size")
    }

}

