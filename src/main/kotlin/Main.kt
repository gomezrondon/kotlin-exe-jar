import java.io.File

//C:\temp

fun main() {

    ///------------ parte 1 indexa
    val folders = listOf<String>("""C:\Users\jrgm\Documents""","""C:\temp""","""C:\CA""")

    folders.forEachIndexed { i, folder ->
        val f_name = "index_$i.txt"
        val indexFile = File(f_name)
        if (indexFile.exists()){
            indexFile.delete()
        }

        File(f_name).bufferedWriter().use { out ->
            File(folder).walkTopDown().filter { it.isFile }.forEach { out.write(it.name +","+ it.absolutePath + "\n") }
        }

    }


  ///------------ parte 2
    /*val regexExec ="""^.*angu.*,""".toRegex(RegexOption.MULTILINE)

    val lines = File("index.txt").readLines()

    val search_result = lines.filter { regexExec.containsMatchIn(it) }

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
        out.write("\n\nNum. Results: $size")
    }*/

}

