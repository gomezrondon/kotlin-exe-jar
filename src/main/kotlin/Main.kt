import java.io.File

fun main(args : Array<String>) {

    if (args.size <= 0 ) return System.exit(1)

    var stashName = args[0].toString().toLowerCase()

    val lines = File(stashName).readLines()

    val regexExec ="""\*\s(.)+""".toRegex(RegexOption.MULTILINE)

    lines.flatMap { regexExec.findAll(it).map { it.value }.toList() }
            .map{it.replace("* ","")}
            .forEach { println(it) }
}


//how to execute
// java -jar current_branch.jar list-branchs.txt