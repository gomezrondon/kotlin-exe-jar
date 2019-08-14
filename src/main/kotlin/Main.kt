import krangl.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun main() {
    val data = DataFrame.readCSV("mock_data.csv")
    //obtenemos una muestra del 30%
    val trainData = data.sampleFrac(0.3)
    //print the schema of the data
    //println(trainData.schema())
//    println(trainData[1])
//    println(trainData["gender"])

    // print each value of the column
    /*    val values = trainData["gender"].values()
      for (itme in values) {
           println(itme) //
       }*/
    // or this way is the same
/*    trainData["gender"].values().forEach {
        println(it)
    }*/

    //this is the correct way to iterate the csv file
/*    val groupBy = trainData.groupBy("gender")
    for (row in groupBy.rows) {
        println(row) //{id=578, first_name=Salvador, time=2018-12-13 08:54:56, last_name=MacRory, gender=Male, ip_address=108.131.123.75}
    }*/

    //--------------------------
    val datePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val firstTime = trainData["time"].asStrings().first()
    val parse = LocalDateTime.parse(firstTime,datePattern)
    println(parse) // test

    //como formatear la time[str] -> hola[LocalDateTime]
    val trainData2 = trainData.addColumns(
            ColumnFormula("hola") {it["time"].map<String>{LocalDateTime.parse(it,datePattern)}}
    )

    println(trainData2.schema())
}



