import krangl.*

//https://github.com/holgerbrandl/krangl

fun main() {
// Create data-frame in memory
    var df: DataFrame = dataFrameOf(
            "first_name", "last_name", "age", "weight")(
            "Max", "Doe", 0, 55,
            "Franz", "Smith", 23, 88,
            "Horst", "Keanes", 12, 70,
            "", "gomez", 35, 80,
            "Javier", "gomez", 35, 80,
            "Javier", "Toro", 20, 90,
            "Mike", "", 35, 80,
            "", "", 0, 0
    )

    //println(df)



    df = df.cleanUp()

    fun divideWeightByAge(w: Int, a: Int): Double {

        return 1.2
    }

    // convert Int -> Double
    df = df.addColumn("age"){ it["age"].map<Double> { it as Double }}
    df = df.addColumn("weight"){ it["weight"].map<Double> { it as Double }}

    // create a new column Test initialize with 0
    df = df.addColumn("Test"){ 0}

    //concat 2 String columns
    df = df.addColumn("full_name") { it["first_name"] + " " + it["last_name"] }


    df = df.addColumn("W_A_relation"){
        df.rows.map { row -> row["weight"] as Double / row["age"]  as Double }
    }
  //  df = df.addColumn("f_last_length"){it["last_name"].map<String>{it.length}} // get length of each element in column
    println(repeatAndCenter("Schema", "-", 70))
     df.schema() // this print the same as println()

    println(repeatAndCenter("Data Frame", "-", 100))
    df.print() // this print the same as println()

    println(repeatAndCenter("Summarize", "-", 50))
    // count columns with the same first name
    val newdf = df.count( "first_name")
    newdf.print()

}

/**
 * function to remove empty strings or ages below 0
 */
 fun DataFrame.cleanUp(): DataFrame = filter {  it["age"].greaterThan(0)}
         .filterByRow { it["first_name"].toString().isNotEmpty()}
         .filterByRow { it["last_name"].toString().isNotEmpty()}


/**
 * function to center a title in a patten of certain size
 */
fun repeatAndCenter(tittle: String, pattern: String, n: Int):String {
    val vN = if (n - tittle.length > 2) {
        n - tittle.length
    } else {
        tittle.length
    }
    val midle = vN / 2

    return pattern.repeat(midle) + tittle + pattern.repeat(midle)
}
