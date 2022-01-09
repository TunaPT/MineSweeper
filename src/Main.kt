fun main() {
    var sair = false
    var column = 0
    var line = 0
    var mines = 0
    var wantLegend = false
    var validou = false
        val invalid = "Invalid response.\n"
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
                    if (legend == "y" || legend == "Y") {
                        wantLegend = true
                    } else if (legend == "n" || legend == "N") {
                        wantLegend = false
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
                    val numOfMines = calculateNumMinesForGameConfiguration(line, column)
                    if (numOfMines != null) {
                        mines = readLine()!!.toIntOrNull() ?: numOfMines
                    }
                    if (!isValidGameMinesConfiguration(line,column,mines)) {
                        println(invalid)
                    }
                } while (!isValidGameMinesConfiguration(line,column,mines))
                val teste = createMatrixTerrain(line, column, mines)
                fillNumberOfMines(teste)
                revealMatrix(teste,0,0)
                val terreno = makeTerrain(teste,wantLegend,false,false)
                println(terreno)
                //fillNumberOfMines(createMatrixTerrain(line, column, mines))
                //println(makeTerrain(createMatrixTerrain(line, column, mines),false,false,false))

                do {
                    println("Choose the Target cell (e.g 2D)")
                    val coords = readLine()
                    val funcao = getCoordinates(coords)
                    if (coords != "exit") {
                        if (funcao != null) {
                            if (isCoordinateInsideTerrain(funcao, column, line)) {
                                if (isMovementPValid(funcao, funcao)) { //acabar aqui
                                    validou = true
                                } else {
                                    println(invalid)
                                }
                            } else {
                                println(invalid)
                            }
                        } else {
                            println(invalid)
                        }
                    } else validou = true
                } while (!validou)
            }
        } while (num != "0" && num != "1")
}

fun makeMenu(): String = "\nWelcome to DEISI Minesweeper\n\n1 - Start New Game\n0 - Exit Game\n"

fun makeTerrain(matrixTerrain: Array<Array<Pair<String, Boolean>>>, showLegend: Boolean, withColor: Boolean, showEverything: Boolean): String {
    var tabuleiroStr = " "
    var count = 0
    var countLegend = 1
    var spaceString = ""
    var linhaSize = matrixTerrain.size
    var doneLegend = false
    val colunaSize = matrixTerrain[matrixTerrain.size-1].size

    for (linha in 0 until matrixTerrain.size) {
        count += 1
        for (coluna in 0 until matrixTerrain[linha].size) {
            spaceString = spaceString + "    " //espaços aqui
            if (coluna < matrixTerrain[linha].size-1) {
                if (showEverything) {
                    if (showLegend) {
                        if (!doneLegend) {
                            doneLegend = true
                            tabuleiroStr += "$countLegend  ${matrixTerrain[linha][coluna].first} | "
                            countLegend += 1
                        } else {
                            tabuleiroStr += "${matrixTerrain[linha][coluna].first} | "
                        }
                    } else {
                        tabuleiroStr += "${matrixTerrain[linha][coluna].first} | "
                    }
                } else {
                    if (matrixTerrain[linha][coluna].second == true) {
                        if (showLegend) {
                            if (!doneLegend) {
                                doneLegend = true
                                tabuleiroStr += "$countLegend  ${matrixTerrain[linha][coluna].first} | "
                                countLegend += 1
                            } else {
                                tabuleiroStr += "${matrixTerrain[linha][coluna].first} | "
                            }
                        } else {
                            tabuleiroStr += "${matrixTerrain[linha][coluna].first} | "
                        }
                    } else {
                        tabuleiroStr += "  | "
                    }
                }
            } else {
                if (showEverything) {
                    tabuleiroStr += "${matrixTerrain[linha][coluna].first}"
                } else {
                    if (matrixTerrain[linha][coluna].second == true) {
                        tabuleiroStr += "${matrixTerrain[linha][coluna].first}"
                    } else {
                        tabuleiroStr += " "
                    }
                }
            }
            tabuleiroStr += ""
        }
        tabuleiroStr += " "
        if (count != matrixTerrain.size) {
            if (!showLegend) {
                tabuleiroStr += "\n"
                for (coluna in 0 until matrixTerrain[linha].size) {
                    if (coluna != matrixTerrain[linha].size) {
                        tabuleiroStr += "---"
                    } else {
                        tabuleiroStr += "---   "
                    }
                    if (coluna < matrixTerrain[linha].size-1) {
                        tabuleiroStr += "+"
                    }
                }
                tabuleiroStr += "\n "
            } else {
                tabuleiroStr += "\n   "
                for (coluna in 0 until matrixTerrain[linha].size) {
                    if (coluna != matrixTerrain[linha].size-1) {
                        tabuleiroStr += "---"
                    } else {
                        tabuleiroStr += "---   "
                    }
                    if (coluna < matrixTerrain[linha].size-1) {
                        tabuleiroStr += "+"
                    }
                }
                tabuleiroStr += "\n $countLegend  "
                countLegend += 1
            }
        }
    }
    if (!showLegend) {
        return tabuleiroStr
    } else {
        val legend2 = createLegend(colunaSize)
        return "    $legend2    \n$tabuleiroStr   \n$spaceString     "
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

fun createMatrixTerrain(numLines: Int, numColumns: Int, numMines: Int, ensurePathToWin: Boolean = false): Array<Array<Pair<String,Boolean>>>{
    var firstPart = arrayOf(Pair("P", true))
    for (k in 0..numColumns - 2){
        firstPart += Pair(" ", false)
    }

    var lastPart = arrayOf(Pair(" ", false))
    for(i in 0..numColumns - 3){
        lastPart += Pair(" ", false)
    }
    lastPart += Pair("f", true)

    var arrayOfPairs = arrayOf(arrayOf(Pair(" ", false)))
    arrayOfPairs.set(0, firstPart)

    for(k in 0..numLines - 3) {
        var array = arrayOf(Pair(" ", false))

        for(i in 0..numColumns - 2){
            array += Pair(" ", false)
        }

        arrayOfPairs += array
    }
    arrayOfPairs += lastPart

    for(k in 0 until numMines){
        var complete = true
        while(complete) {
            val randomInLine = (0 until numLines).random()
            val randomInColumn = (0 until numColumns).random()

            val placingMine = arrayOfPairs[randomInLine][randomInColumn]

                if (placingMine == Pair(" ", false)) {
                    arrayOfPairs[randomInLine][randomInColumn] = Pair("*", false)
                    complete = false
                }
        }
    }
    return arrayOfPairs
}

fun countNumberOfMinesCloseToCurrentCell(matrixTerrain: Array<Array<Pair<String, Boolean>>>, centerY: Int, centerX: Int): Int {
    var count = 0
    if (centerY > 0 && centerX > 0) {
        if (matrixTerrain[centerY - 1][centerX - 1].first == "*") {
            count += 1
        }
    }
    if (centerY > 0 && centerX < matrixTerrain.size-1) {
        if (matrixTerrain[centerY - 1][centerX + 1].first == "*") {
            count += 1
        }
    }
    if (centerY < matrixTerrain.size-1 && centerX > 0) {
        if (matrixTerrain[centerY + 1][centerX - 1].first == "*") {
            count += 1
        }
    }
    if (centerY < matrixTerrain.size-1 && centerX < matrixTerrain.size-1) {
        if (matrixTerrain[centerY + 1][centerX + 1].first == "*") {
            count += 1
        }
    }
    // Check Above, Below, Sides
    if (centerY < matrixTerrain.size-1) {
        if (matrixTerrain[centerY + 1][centerX].first == "*") {
            count += 1
        }
    }
    if (centerY > 0) {
        if (matrixTerrain[centerY - 1][centerX].first == "*") {
            count += 1
        }
    }
    if (centerX > 0) {
        if (matrixTerrain[centerY][centerX - 1].first == "*") {
            count += 1
        }
    }
    if (centerX < matrixTerrain.size-1) {
        if (matrixTerrain[centerY][centerX + 1].first == "*") {
            count += 1
        }
    }
    return count
}

fun fillNumberOfMines(matrixTerrain: Array<Array<Pair<String, Boolean>>>) {
    for (linha in 0 until matrixTerrain.size) {
        for (coluna in 0 until matrixTerrain[linha].size) {
            if (coluna < matrixTerrain[linha].size-1) {
                if (matrixTerrain[linha][coluna].first == " ") {
                    val numMine = countNumberOfMinesCloseToCurrentCell(matrixTerrain, linha, coluna)
                    if (numMine != 0) {
                        matrixTerrain[linha][coluna] = Pair("$numMine", false)
                    }
                }
            } else {
                if (matrixTerrain[linha][coluna].first == " ") {
                    val numMine2 = countNumberOfMinesCloseToCurrentCell(matrixTerrain, linha, coluna)
                    if (numMine2 != 0) {
                        matrixTerrain[linha][coluna] = Pair("$numMine2", false)
                    }
                }
            }
        }
    }
}

fun revealMatrix(matrixTerrain: Array<Array<Pair<String, Boolean>>>, coordY: Int, coordX: Int, endGame: Boolean = false) {
    var numeroAtual = ""
    if (coordY > 0 && coordX > 0) {
        if (matrixTerrain[coordY - 1][coordX - 1].first != "*") {
            numeroAtual = matrixTerrain[coordY - 1][coordX - 1].first
            matrixTerrain[coordY - 1][coordX - 1] = Pair("$numeroAtual", true)
        }
    }
    if (coordY > 0 && coordX < matrixTerrain.size-1) {
        if (matrixTerrain[coordY - 1][coordX + 1].first != "*") {
            numeroAtual = matrixTerrain[coordY - 1][coordX + 1].first
            matrixTerrain[coordY - 1][coordX + 1] = Pair("$numeroAtual", true)
        }
    }
    if (coordY < matrixTerrain.size-1 && coordX > 0) {
        if (matrixTerrain[coordY + 1][coordX - 1].first != "*") {
            numeroAtual = matrixTerrain[coordY + 1][coordX - 1].first
            matrixTerrain[coordY + 1][coordX - 1] = Pair("$numeroAtual", true)
        }
    }
    if (coordY < matrixTerrain.size-1 && coordX < matrixTerrain.size-1) {
        if (matrixTerrain[coordY + 1][coordX + 1].first != "*") {
            numeroAtual = matrixTerrain[coordY + 1][coordX + 1].first
            matrixTerrain[coordY + 1][coordX + 1] = Pair("$numeroAtual", true)
        }
    }
    // Check Above, Below, Sides
    if (coordY < matrixTerrain.size-1) {
        if (matrixTerrain[coordY + 1][coordX].first != "*") {
            numeroAtual = matrixTerrain[coordY + 1][coordX].first
            matrixTerrain[coordY + 1][coordX] = Pair("$numeroAtual", true)
        }
    }
    if (coordY > 0) {
        if (matrixTerrain[coordY - 1][coordX].first != "*") {
            numeroAtual = matrixTerrain[coordY - 1][coordX].first
            matrixTerrain[coordY - 1][coordX] = Pair("$numeroAtual", true)
        }
    }
    if (coordX > 0) {
        if (matrixTerrain[coordY][coordX - 1].first != "*") {
            numeroAtual = matrixTerrain[coordY][coordX - 1].first
            matrixTerrain[coordY][coordX - 1] = Pair("$numeroAtual", true)
        }
    }
    if (coordX < matrixTerrain.size-1) {
        if (matrixTerrain[coordY][coordX + 1].first != "*") {
            numeroAtual = matrixTerrain[coordY][coordX + 1].first
            matrixTerrain[coordY][coordX + 1] = Pair("$numeroAtual", true)
        }
    }
}

fun isEmptyAround(matrixTerrain: Array<Array<Pair<String, Boolean>>>, centerY: Int, centerX: Int, yl: Int, xl: Int, yr: Int, xr: Int): Boolean {
    var maxLinha = matrixTerrain.size
    var maxColuna = 0
    for (linha in 0 until matrixTerrain.size) {
        for (coluna in 0 until matrixTerrain[linha].size) {
            maxColuna = matrixTerrain[linha].size
        }
    }
    return false
}

fun isMovementPValid(currentCoord : Pair<Int, Int>, targetCoord : Pair<Int, Int>): Boolean {
    if (targetCoord.first+1 == currentCoord.first || targetCoord.first-1 == currentCoord.first || targetCoord.first == currentCoord.first) {
        if (targetCoord.second+1 == currentCoord.second || targetCoord.second-1 == currentCoord.second || targetCoord.second == currentCoord.second) {
            return true
        } else return false
    } else return false
}

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

fun getSquareAroundPoint(linha: Int, coluna: Int, numLines: Int, numColumns: Int): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    var linha1 = 0
    var linha2 = 0
    var coluna1 = 0
    var coluna2 = 0
    if (linha > 0) {
        linha1 = linha-1
    } else {
        linha1 = linha
    }
    if (linha < numLines) {
        linha2 = linha+1
    } else {
        linha2 = linha
    }
    if (coluna > 0) {
        coluna1 = coluna-1
    } else {
        coluna1 = coluna
    }
    if (coluna < numColumns) {
        coluna2 = coluna+1
    } else {
        coluna2 = coluna
    }
    return Pair(Pair(linha1,coluna1),Pair(linha2,coluna2))
}
