fun main() {
    val invalid = "Invalid response.\n"
    var column = 0
    var line = 0
    var mines = 0
    do {
        println(makeMenu())
        val num = readLine()
        if (num != "0" && num != "1") {
            println(invalid)
        }
        if (num == "1") {
            do {
                println("Enter player name?")
                val name = readLine()
                if (!isNameValid(name, 3)) {
                    println(invalid)
                }
            } while (!isNameValid(name, 3))

            do {
                println("Show legend (y/n)?")
                val legend = readLine()!!
                if (legend != "y" && legend != "n" && legend != "Y" && legend != "N") {
                    println(invalid)
                }
            } while (legend != "y" && legend != "n" && legend != "Y" && legend != "N")

            do {
                println("How many lines?")
                line = readLine()!!.toInt()
                if (line != 1) {
                    println(invalid)
                }
            } while (line != 1)

            do {
                println("How many columns?")
                column = readLine()!!.toInt()
                if (column < 3) {
                    println(invalid)
                }
            } while (column < 3)

            do {
                println("How many mines (press enter for default value)?")
                mines = readLine()?.toIntOrNull() ?: 3
            } while (!isValidGameMinesConfiguration(line,column,mines))
            println(makeTerrain(line, column, mines))
        }
    } while (num != "0" && num != "1")
}

fun makeMenu(): String = "\nWelcome to DEISI Minesweeper\n\n1 - Start New Game\n0 - Exit Game\n"

fun makeTerrain(numLines: Int, numColumns: Int, numMines: Int, showLegend: Boolean = true, withColor: Boolean = false): String {
    val esc: String = "\u001B"
    val legendColor = "$esc[97;44m"
    val endLegendColor = "$esc[0m"
    val terreno = numLines*numColumns
    var lastLineCount = 0
    var lastLineString = ""
    var count = numMines
    var spaces = 0
    var stringMines = ""
    while (lastLineCount != terreno) { //Adiciona os espaços na ultima linha
        lastLineCount++
        lastLineString = lastLineString + "    "
    }
    while (count != 0) {
        count -= 1
        spaces += 1
        stringMines = stringMines + "| * "
    }
    while (spaces != numColumns - 2) {
        spaces += 1
        stringMines = stringMines + "|   "
    }
    if (!showLegend) {
        return " P " + stringMines + "| f "
    } else {
        val legend2 = createLegend(numColumns)
        val terrain = " P " + stringMines + "| f "
        if (!withColor) {
            return "    $legend2    \n $numLines $terrain   \n$lastLineString     "
        } else {
            val legendWithColor = "$legendColor    $legend2    $endLegendColor\n" +
                    "$legendColor $numLines $endLegendColor$terrain$legendColor   $endLegendColor\n" +
                    "$legendColor$lastLineString     $endLegendColor"
            return legendWithColor
        }
    }
}

fun isNameValid(name: String?, minLength: Int = 3): Boolean {
    if (name != null) {
        var position = 0
        if (" " in name) {
            while (name[position] != ' ') {
                position++
            }
            if (name.length > position) {
                if (name[1] != ' ' && name[2] != ' ' && name[0].isUpperCase() && name[position+1].isUpperCase()) {
                    return true
                } else return false
            } else return false
        } else return false
    } else return false
}

fun calculateNumMinesForGameConfiguration(numLines: Int, numColumns: Int): Int? {
    val casasVazias = numLines*numColumns-2
    if (casasVazias == 1) {
        return 1
    } else if (casasVazias in 2..5) {
        return 2
    } else if (casasVazias in 6..10) {
        return 3
    } else if (casasVazias in 11..20) {
        return 6
    } else if (casasVazias in 21..50) {
        return 10
    } else if (casasVazias >= 51) {
        return 15
    } else return null
}

fun createLegend(numColumns: Int): String {
    val legendLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    var legendString = "A"
    var start = 1
    while (legendLetter[start] != legendLetter[numColumns]) {
        legendString = legendString + "   " + legendLetter[start]
        start += 1
    }
    return legendString
}

fun isValidGameMinesConfiguration(numLines: Int, numColumns: Int, numMines: Int): Boolean {
    val casasVazias = numLines*numColumns-2
    if (numMines <= 0 || casasVazias < numMines) {
        return false
    } else return true
}