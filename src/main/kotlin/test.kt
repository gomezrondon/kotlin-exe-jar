import java.io.File


fun main() {
/*
    val climate = dataFrameOf(
            "city", "coast_distance", "1995", "2000", "2005")(
            "Dresden", 400, 343, 252, 423,
            "Frankfurt", 534, 534, 435, 913)

    var df = climate.select("city", "coast_distance")
    df.print()
*/

    val colors = arrayOf("amaranth", "coquelicot", "smaragdine")
    println("Random array element: ${colors.random()}")

    val flavors = listOf("maple", "bacon", "lemon curry")
    println("Random list item: ${flavors.random()}")

}