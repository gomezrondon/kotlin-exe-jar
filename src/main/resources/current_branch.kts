import java.io.File

var stashName = args[0].toLowerCase()

val lines = File(stashName).readLines()

val regexExec ="""\*\s(.)+""".toRegex(RegexOption.MULTILINE)

lines.flatMap { regexExec.findAll(it).map { it.value }.toList() }
         .map{it.replace("* ","")}
         .forEach { println(it) }


//how to execute:
//> kotlinc -script current_branch.kts list-branchs.txt