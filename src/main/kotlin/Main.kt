import java.io.File

fun main(args : Array<String>) {
    var stashName = args[0].toString().toLowerCase()

    val lines = File(stashName).readLines()

    val regexExec ="""\*\s(.)+""".toRegex(RegexOption.MULTILINE)

    lines.flatMap { regexExec.findAll(it).map { it.value }.toList() }
            .map{it.replace("* ","")}
            .forEach { println(it) }
}
