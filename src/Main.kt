fun main() {
    println("Welcome to DEISI Minesweeper")
    println(makeMenu())
    var num = readLine()
    while (num != "0" && num != "1") {
        println("Invalid response.\n")
        num = readLine()
    }
    if (num == "1") {

        do {
            println("Enter player name?")
            val name = readLine()
            if (!isNameValid(name, 3)) {
                println("Invalid response.\n")
            }
        } while (!isNameValid(name, 3))

        do {
            println("Show legend (y/n)?")
            val legend = readLine()!!
            if (legend != "y" && legend != "n" && legend != "Y" && legend != "N") {
                println("Invalid response.\n")
            }
        } while (legend != "y" && legend != "n" && legend != "Y" && legend != "N")

        do {
            println("How many lines?")
            val line = readLine()!!
            if (line != "1") {
                println("Invalid response.\n")
            }
        } while (line != "1")

        do {
            println("How many columns?")
            val column = readLine()!!
            if (column < "3") {
                println("Invalid response.\n")
            }
        } while (column < "3")

        println("How many mines (press enter for default value)?")
        val mines = readLine()?.toIntOrNull()?:3
    }
}

fun makeMenu(): String = "1 - Start New Game\n0 - Exit Game"

fun makeTerrain(numLines: Int, numColumns: Int, numMines: Int, showLegend: Boolean = true, withColor: Boolean = false): String = ""

fun isNameValid(name: String?, minLength: Int = 3): Boolean {
    if (name != null) {
        var position = 0
        while (name[position] != ' ') {
            position++
        }
        return name[2] != ' ' && " " in name && name[0].isUpperCase() && name[position+1].isUpperCase()
    } else return false
}

fun calculateNumMinesForGameConfiguration(numLines: Int, numColumns: Int): Int? = 0

fun createLegend(numColumns: Int): String = ""

fun isValidGameMinesConfiguration(numLines: Int, numColumns: Int, numMines: Int): Boolean = false

/*
        if (name[2] != ' ' && " " in name && name[0].isUpperCase() && name[position+1].isUpperCase()) {
            return true
        } else {
            return false
        }

        return name[2] != ' ' && " " in name && name[0].isUpperCase() && name[position+1].isUpperCase()
 */
