
import java.io.File
import kotlin.system.measureTimeMillis

//val whitelist = "txt,cs,html,sln,csproj,config,resx,settings,pas,dpr,dproj,res,dfm,ini,java,gradle".split(",")

fun getWhiteList(WhiteListFileName: String): List<String> {

    return if (File(WhiteListFileName).exists()) {
        File(WhiteListFileName).readLines().flatMap { it.split(",") }
    } else {
        listOf()
    }

}


fun createFileWithFileNames(outFileName: String, searchDirectory:String, reBuild:Boolean=false) {

    println("Rebuilding temp file: <$reBuild>")
    println("")
    val time = measureTimeMillis {
        if (!File(outFileName).exists() || reBuild) {

            val whitelist =  getWhiteList("whitelist.txt")

            val list = File(searchDirectory).walkBottomUp()//3533 .walk() //3970
                    .filter { it.isFile }
                    .filter { whitelist.contains(it.extension) }
                    .toList()

            File(outFileName).printWriter().use { out ->
                list.forEach {
                    out.println(it.absolutePath)
                }
            }
        }
    }
    println("total time: $time")
}


fun getListOfFiles(outFileName: String): List<File> {
    return if (File(outFileName).exists()) {
        File(outFileName).readLines().map { File(it) }.toList()
    } else {
        listOf()
    }
}