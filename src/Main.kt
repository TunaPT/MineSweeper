fun main() {
    val invalid = "Invalid response.\n"
    var wantLegend = false
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
                if (!isNameValid(name,3)) {
                    println(invalid)
                }
            } while (!isNameValid(name,3))
//Diferença entre minLength = 3 e "minLength:3" ?
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
//Dúvida: Lines e colunas nao tem de ser var/val ?

            do {
                println("How many mines (press enter for default value)?")
                mines = readLine()?.toIntOrNull() ?: 3
            } while (!isValidGameMinesConfiguration(line,column,mines))
 //Dúvida: Bool !isValidGameMinesConfiguration
            println(makeTerrain(line, column, mines))
        }
    } while (num != "0" && num != "1")
}

fun makeMenu(): String = "\nWelcome to DEISI Minesweeper\n\n1 - Start New Game\n0 - Exit Game\n"

fun makeTerrain(numLines: Int, numColumns: Int, numMines: Int, showLegend: Boolean = true, withColor: Boolean = false): String {
    val terreno = numLines*numColumns
    var espaco = 0
    var spaceString = ""
    var count = numMines
    var spaces = 0
    var numeroMines = ""
    while (espaco != terreno) {
        espaco++
        spaceString = spaceString + "    "
    }
    while (count != 0) {
        count -= 1
        spaces += 1
        numeroMines = numeroMines + "| * "
    }
    while (spaces != numColumns - 2) {
        spaces += 1
        numeroMines = numeroMines + "|   "
    }
    if (!showLegend) {
        return " P " + numeroMines + "| f "
    } else {
        val legend2 = createLegend(numColumns)
        val terrain = " P " + numeroMines + "| f "
        return "    $legend2    \n $numLines $terrain   \n$spaceString     "
    }
}
//Não entendi spaceString !
//Minas ainda não necessitam de random certo ?

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
//Possivel erro mas acho que não entra nos requesitos da função.
//Upper cases a balda + MinLenght não é respeitado

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
//Não entendi como a função createLegend funciona.
fun isValidGameMinesConfiguration(numLines: Int, numColumns: Int, numMines: Int): Boolean {
    val casasVazias = numLines*numColumns-2
    if (numMines <= 0 || casasVazias < numMines) {
        return false
    } else return true
}