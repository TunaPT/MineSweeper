fun main() {
}

fun test() {
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
                if (line !in 4..9) {
                    println(invalid)
                }
            } while (line !in 4..9)

            do {
                println("How many columns?")
                column = readLine()!!.toInt()
                if (column !in 4..9) {
                    println(invalid)
                }
            } while (column !in 4..9)

            do {
                println("How many mines (press enter for default value)?")
                mines = readLine()?.toIntOrNull() ?: 6
            } while (!isValidGameMinesConfiguration(line,column,mines))
            //println(makeTerrain(line, column, mines))
        }
    } while (num != "0" && num != "1")
}

fun makeMenu(): String = "\nWelcome to DEISI Minesweeper\n\n1 - Start New Game\n0 - Exit Game\n"

fun makeTerrain(matrixTerrain: Array<Array<Pair<String, Boolean>>>, showLegend: Boolean, withColor: Boolean, showEverything: Boolean): String = "" /*{
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
}*/

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
    if (casasVazias in 14..20) {
        return 6
    } else if (casasVazias in 21..40) {
        return 9
    } else if (casasVazias in 41..60) {
        return 12
    } else if (casasVazias in 61..79) {
        return 19
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

fun createMatrixTerrain(numLines: Int, numColumns: Int, numMines: Int, ensurePathToWin: Boolean = false): Array<Array<Pair<String, Boolean>>> = arrayOf(
    arrayOf(Pair("",false)))

fun countNumberOfMinesCloseToCurrentCell(matrixTerrain: Array<Array<Pair<String, Boolean>>>, centerY: Int, centerX: Int): Int = 2

fun fillNumberOfMines(matrixTerrain: Array<Array<Pair<String, Boolean>>>) = 2

fun revealMatrix(matrixTerrain: Array<Array<Pair<String, Boolean>>>, coordY: Int, coordX: Int, endGame: Boolean = false) = false

fun isEmptyAround(matrixTerrain: Array<Array<Pair<String, Boolean>>>, centerY: Int, centerX: Int, yl: Int, xl: Int, yr: Int, xr: Int): Boolean = false

fun isMovementPValid(currentCoord : Pair<Int, Int>, targetCoord : Pair<Int, Int>): Boolean = false

fun isCoordinateInsideTerrain(coord: Pair<Int, Int>, numColumns: Int, numLines: Int): Boolean {
    if (coord.first < numColumns && coord.second < numLines) {
        if (coord.first >= 0 && coord.second >= 0) {
            return true
        } else {
            return false
        }
    } else {
        return false
    }
}

fun getCoordinates (readText: String?): Pair<Int, Int>? {
    if (readText != null && readText.length == 2) {
        var count = 0
        if (readText[0].isDigit()) {
            val valor = readText[0].toInt() - 49
            if (readText[1].isLetter()) {
                val legendLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                val legendLetter2 = "abcdefghijklmnopqrstuvwxyz"
                while ((readText[1] != legendLetter[count]) && (readText[1] != legendLetter2[count])) {
                    if ((count != legendLetter.length-1) && (count != legendLetter2.length-1)) {
                        count += 1
                    }
                }
                if (valor >= 0) {
                    return Pair(valor, count)
                } else {
                    return null
                }
            } else {
                return null
            }
        } else {
            return null
        }
    }
    return null
}

fun getSquareAroundPoint(linha: Int, coluna: Int, numLines: Int, numColumns: Int): Pair<Pair<Int, Int>, Pair<Int, Int>> = Pair(Pair(2,2),Pair(2,2))
