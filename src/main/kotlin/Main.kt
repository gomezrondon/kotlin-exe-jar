import java.io.File
import java.lang.ProcessBuilder.Redirect
import java.util.concurrent.TimeUnit

//"cmd.exe /c gradle clean build".runCommand(timeout = 60)

fun main() {
    File("salida.txt").writeText("empty")

    "cmd.exe /c tasklist".runCommand(timeout = 60)
    val filter = File("salida.txt").readLines().filter { it.startsWith("SnippingTool.exe") }

    if (filter.size > 0) {
        println("la aplicacion existe!, I will kill it")
        Runtime.getRuntime().exec("cmd.exe /c taskkill /F /IM SnippingTool.exe");
    }else{
        println("Ejecutando aplicacion")
        Runtime.getRuntime().exec("cmd.exe /c start %windir%\\system32\\SnippingTool.exe");// execute and continue in another thread
    }

}

fun String.runCommand(workingDir: File? = null, timeout:Long) {
    val process = ProcessBuilder(*split(" ").toTypedArray())
            .directory(workingDir)
            .redirectOutput(Redirect.appendTo(File("salida.txt")))
            .redirectError(Redirect.INHERIT)
            .start()

    // File("salida.txt").readLines().forEach { println(it) }

    if (!process.waitFor(timeout, TimeUnit.SECONDS)) {
        process.destroy()
        throw RuntimeException("execution timed out: $this")
    }
    if (process.exitValue() != 0) {
        throw RuntimeException("execution failed with code ${process.exitValue()}: $this")
    }


}