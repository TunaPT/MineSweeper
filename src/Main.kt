fun main() {
    val invalid = "Invalid response.\n"
    var wantLegend = false
    var column = 0
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
                } else {
                    wantLegend = true
                }
            } while (legend != "y" && legend != "n" && legend != "Y" && legend != "N")

            do {
                println("How many lines?")
                val line = readLine()!!
                if (line != "1") {
                    println(invalid)
                }
            } while (line != "1")

            do {
                println("How many columns?")
                column = readLine()!!.toInt()
                if (column < 3) {
                    println(invalid)
                }
            } while (column < 3)

            println("How many mines (press enter for default value)?")
            val mines = readLine()?.toIntOrNull()?:3
            if (wantLegend) {
                createLegend(column)
                }
        }
    } while (num != "0" && num != "1")
}

fun makeMenu(): String = "\nWelcome to DEISI Minesweeper\n\n1 - Start New Game\n0 - Exit Game\n"  //Função Correta

fun makeTerrain(numLines: Int, numColumns: Int, numMines: Int, showLegend: Boolean = true, withColor: Boolean = false): String = ""

fun isNameValid(name: String?, minLength: Int = 3): Boolean {
    if (name != null) {
        var position = 0
        if (" " in name) {
            while (name[position] != ' ') {
                position++
            }
            if (name.length > position) {
                if (name[2] != ' ' && name[0].isUpperCase() && name[position+1].isUpperCase()) {
                    return true
                } else return false
            } else return false
        } else return false
    } else return false
}

//Função isNameValid a compilar corretamente

fun calculateNumMinesForGameConfiguration(numLines: Int, numColumns: Int): Int? = 0

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

//Função createLegend a compilar corretamente

fun isValidGameMinesConfiguration(numLines: Int, numColumns: Int, numMines: Int): Boolean = false