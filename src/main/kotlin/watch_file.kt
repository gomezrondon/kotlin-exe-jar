import java.io.File
//import kotlin.io.copyTo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun main() {

    val filePath = "C:\\Users\\***\\Pictures\\Capture.PNG"
    val file = File(filePath)
    val absolutePath = file.parent
    val fName = file.name

    while (true) {
        if (file.exists()) {
            Thread.sleep(1000)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmmss")
            val strTime = LocalDateTime.now().format(formatter)
            // println("Sleeping ZZZ  $fName,  $absolutePath $strTime")
            val fileNewName = fName.split(".")[0] + strTime +"."+ file.extension
            val renameTo = file.renameTo(File(absolutePath+File.separator+fileNewName))
            if (renameTo) {
                println(fileNewName)
            }

        }
    }

}