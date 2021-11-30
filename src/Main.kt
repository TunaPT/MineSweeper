fun main() {
    println("Welcome to DEISI Minesweeper")
    println(makeMenu())
    var num = readLine()
    while (num != "0" && num != "1") {
        println("Invalid response.\n")
        println(makeMenu())
        num = readLine()
    }
    if (num == "1") {
        println("Enter player name?")
        var name = readLine()
        var lenght = name!!.length
        while (lenght < 3) {
            println("Invalid response.\n")
            println("Enter player name?")
            name = readLine()
            lenght = name!!.length
        }
    }
}

fun makeMenu(): String = "1 - Start New Game\n0 - Exit Game"

fun makeTerrain(numLines: Int, numColumns: Int, numMines: Int, showLegend: Boolean = true, withColor: Boolean = false): String = ""

fun isNameValid(name: String?, minLength: Int): Boolean = false

fun calculateNumMinesForGameConfiguration(numLines: Int, numColumns: Int): Int? = 0

fun createLegend(numColumns: Int): String = ""

fun isValidGameMinesConfiguration(numLines: Int, numColumns: Int, numMines: Int): Boolean = false