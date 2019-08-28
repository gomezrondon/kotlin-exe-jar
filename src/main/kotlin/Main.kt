import java.io.File

fun main() {

    val primes = listOf<Int>(1, 3 , 5,7,9,13)

    primes.forEachIndexed { i, prime -> println("Prime #${i+1} is: $prime")}
    primes.stream().map { it }
}


//how to execute
// java -jar current_branch.jar list-branchs.txt