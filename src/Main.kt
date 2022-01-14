var column = 0
var line = 0
var mines = 0
var wantLegend = false
val invalid = "Invalid response.\n"

fun main() {
    do {
        println(makeMenu())
        val num = readLine()
        if (num != "0" && num != "1") println(invalid)
        if (num == "1") {
            do {
                println("Enter player name?")
                val name = readLine()
                if (!isNameValid(name, 3)) println(invalid)
            } while (!isNameValid(name, 3))
            do {
                println("Show legend (y/n)?")
                val legend = readLine()!!
                if (legend != "y" && legend != "n" && legend != "Y" && legend != "N") println(invalid)
                if (legend == "y" || legend == "Y") {
                    wantLegend = true
                } else if (legend == "n" || legend == "N") {
                    wantLegend = false
                }
            } while (legend != "y" && legend != "n" && legend != "Y" && legend != "N")
            do {
                println("How many lines?")
                line = readLine()!!.toInt()
                if (line !in 4..9) println(invalid)
            } while (line !in 4..9)
            do {
                println("How many columns?")
                column = readLine()!!.toInt()
                if (column !in 4..9) println(invalid)
            } while (column !in 4..9)
            do {
                println("How many mines (press enter for default value)?")
                val numOfMines = calculateNumMinesForGameConfiguration(line, column)
                if (numOfMines != null) mines = readLine()!!.toIntOrNull() ?: numOfMines
                if (!isValidGameMinesConfiguration(line,column,mines)) println(invalid)
            } while (!isValidGameMinesConfiguration(line,column,mines))
            val matrixTerrain = createMatrixTerrain(line, column, mines)
            fillNumberOfMines(matrixTerrain)
            revealMatrix(matrixTerrain,0,0) // revela os numeros (se existir) à volta da posição inicial
            val terreno = makeTerrain(matrixTerrain,wantLegend,false,false)
            println(terreno)
            funcaoTargetCell(matrixTerrain) // funçao de movimentar
        }
    } while (num != "0" && num != "1")
}

fun funcaoTargetCell(matrixTerrrain: Array<Array<Pair<String,Boolean>>>) {
    val matrix = Array(line) { Array(column) { Pair(" ", false) } }
    var cordenadas: Pair<Int,Int>?
    var actualPosition = Pair(0,0)
    var coordsAnterior = Pair(matrix[0][0].first,true) // Pair<String, Boolean>
    do {
        do {
            println("Choose the Target cell (e.g 2D)")
            val coords = readLine()
            cordenadas = getCoordinates(coords) // retorna Pair<Int, Int>? se verificar-se que é válido
            if (coords == "exit") return
            if (coords == "abracadabra") { // mostra o terreno completo
                println(makeTerrain(matrixTerrrain, wantLegend, showEverything = true))
            } else {
                if (cordenadas == null || !isMovementPValid(actualPosition, cordenadas) || !isCoordinateInsideTerrain(
                        cordenadas, line, column)) println(invalid)
            }
        } while (cordenadas == null || !isMovementPValid(actualPosition, cordenadas) || !isCoordinateInsideTerrain(
                cordenadas, line, column))
        if (matrixTerrrain[cordenadas.first][cordenadas.second].first == "*") { // se a posição escolhida for mina perde o jogo
            println(makeTerrain(matrixTerrrain, wantLegend, showEverything = true))
            println("You lost the game!")
            return main()
        } else if (matrixTerrrain[cordenadas.first][cordenadas.second].first == "f") { // se a posição escolhida for igual à casa final ganha o jogo
            println(makeTerrain(matrixTerrrain, wantLegend, showEverything = true))
            println("You win the game!")
            return main()
        }
        actualPosition = Pair(cordenadas.first,cordenadas.second)
        revealMatrix(matrixTerrrain,cordenadas.first, cordenadas.second) // revela os numeros à volta da posiçao atual, conforme vai andando
        coordsAnterior = Pair(matrixTerrrain[cordenadas.first][cordenadas.second].first,true)
        matrixTerrrain[cordenadas.first][cordenadas.second] = Pair("P",true)
        matrixTerrrain[0][0] = Pair(" ",true) // altera o valor da posição inicial após movimentar-se pela primeira vez
        println(makeTerrain(matrixTerrrain,wantLegend, showEverything = false)) // cria novo terreno com as condições novas
        matrixTerrrain[cordenadas.first][cordenadas.second] = coordsAnterior
    } while (true)
}

fun makeMenu(): String = "\nWelcome to DEISI Minesweeper\n\n1 - Start New Game\n0 - Exit Game\n"

fun makeTerrain(matrixTerrain: Array<Array<Pair<String, Boolean>>>, showLegend: Boolean, withColor: Boolean = false,
                showEverything: Boolean): String {
    var tabuleiroStr = " "
    var count = 0
    var countLegend = 1
    var spaceString = ""
    var doneLegend = false
    val colunaSize = matrixTerrain[matrixTerrain.size-1].size
    var spaceAdd = ""

    for (linha in 0 until matrixTerrain.size) { count += 1
        for (coluna in 0 until matrixTerrain[linha].size) {
            if (coluna < matrixTerrain[linha].size-1) { spaceAdd = "${matrixTerrain[linha][coluna].first} | "
                if (showEverything) {
                    if (showLegend) {
                        if (!doneLegend) {
                            doneLegend = true
                            tabuleiroStr += "$countLegend  ${matrixTerrain[linha][coluna].first} | "
                            countLegend += 1
                        } else tabuleiroStr += spaceAdd
                    } else tabuleiroStr += spaceAdd
                } else if (matrixTerrain[linha][coluna].second == true) {
                    if (showLegend) {
                        if (!doneLegend) {
                            doneLegend = true
                            tabuleiroStr += "$countLegend  ${matrixTerrain[linha][coluna].first} | "
                            countLegend += 1
                        } else tabuleiroStr += spaceAdd
                    } else tabuleiroStr += spaceAdd
                } else tabuleiroStr += "  | "
            } else if (showEverything) { tabuleiroStr += matrixTerrain[linha][coluna].first
            } else if (matrixTerrain[linha][coluna].second == true) { tabuleiroStr += matrixTerrain[linha][coluna].first
            } else tabuleiroStr += " "
            tabuleiroStr += ""
        }
        tabuleiroStr += " "
        if (count != matrixTerrain.size) {
            if (!showLegend) { tabuleiroStr += "\n"
                for (coluna in 0 until matrixTerrain[linha].size) {
                    if (coluna != matrixTerrain[linha].size) { tabuleiroStr += "---"
                    } else tabuleiroStr += "---   "
                    if (coluna < matrixTerrain[linha].size-1) tabuleiroStr += "+"
                }
                tabuleiroStr += "\n "
            } else {
                tabuleiroStr += "   \n   "
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
    spaceString = "    ".repeat(colunaSize) + "     "
    if (!showLegend) {
        return tabuleiroStr
    } else {
        val legend2 = createLegend(colunaSize)
        return "    $legend2    \n$tabuleiroStr   \n$spaceString"
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

    val matrix = Array(numLines){Array(numColumns){Pair(" ", false)} }  // Array de Array de Pairs
    matrix[0][0] = Pair("P",true) // Altera valor da Primeira Posição
    matrix[numLines-1][numColumns-1] = Pair("f",true) // Altera valor da Última Posição

    var minesNum = numMines  // numMines é uma variável imutável, pelo que foi associado o mesmo valor a outra variável
    if (!ensurePathToWin) { // Modo predefinido
        while (minesNum > 0) {
            val randomInLine = (0 until numLines).random() // 0 in numLines -1
            val randomInColumn = (0 until numColumns).random()
            if (matrix[randomInLine][randomInColumn].first == " ") { // Vai confirmar se o primeiro valor do Pair correspondente à posição atual, originada de forma random, é um espaço vazio
                matrix[randomInLine][randomInColumn] = Pair("*",false) // Preenche a posição com uma mina se for um espaço vazio
                minesNum--
            }
        }
    } else {
        while (minesNum > 0) {
            val randomInLine = (0 until numLines).random()
            val randomInColumn = (0 until numColumns).random()
            val squarePoint = getSquareAroundPoint(randomInLine,randomInColumn,numLines,numColumns) // retorna Pair<Pair<Int, Int>, Pair<Int, Int>> | Pair(Pair(yleft,xleft),Pair(yright,xright))
            val yleft = squarePoint.first.first // Vai buscar a respetiva posição no Pair
            val xleft = squarePoint.first.second
            val yright = squarePoint.second.first
            val xright = squarePoint.second.second
            if (matrix[randomInLine][randomInColumn].first == " ") { // Caso a posição aleatória seja uma casa vazia, avança para a verificação do campo à volta dessa posição
                if (isEmptyAround(matrix, randomInLine, randomInColumn, yleft, xleft, yright, xright)) {
                    matrix[randomInLine][randomInColumn] = Pair("*", false) // Estando vazio à volta, aplica uma mina nessa posição
                    minesNum--
                }
            }
        }
    }
    return matrix
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
            if (matrixTerrain[linha][coluna].first == " ") {
                val numMine = countNumberOfMinesCloseToCurrentCell(matrixTerrain, linha, coluna)
                if (numMine != 0) {
                    matrixTerrain[linha][coluna] = Pair("$numMine", false) // se a posição for uma casa vazia e tiver minas à volta, coloca o número de minas à volta
                }
            }
        }
    }
}

fun revealMatrix(matrixTerrain: Array<Array<Pair<String, Boolean>>>, coordY: Int, coordX: Int, endGame: Boolean = false) {
    val squarePoint = getSquareAroundPoint(coordY,coordX, matrixTerrain.size, matrixTerrain[matrixTerrain.size-1].size)
    val yleft = squarePoint.first.first
    val xleft = squarePoint.first.second
    val yright = squarePoint.second.first
    val xright = squarePoint.second.second

    for (linha in yleft..yright) {
        for (coluna in xleft..xright) {
            if (!endGame) {
                if (matrixTerrain[linha][coluna].first != "*") { // se à volta for diferente de uma mina, revela essa casa
                    matrixTerrain[linha][coluna] = Pair(matrixTerrain[linha][coluna].first, true)
                }
            } else { // se o jogo acabar revela o tabuleiro completo
                matrixTerrain[linha][coluna] = Pair(matrixTerrain[linha][coluna].first, true)
            }
        }
    }
}

fun isEmptyAround(matrixTerrain: Array<Array<Pair<String, Boolean>>>, centerY: Int, centerX: Int, yl: Int, xl: Int, yr: Int, xr: Int): Boolean {
    for (linha in yl..yr) {
        for (coluna in xl..xr) {
            val terrain = matrixTerrain[linha][coluna].first
            if (!(centerY == linha && centerX == coluna) && (terrain == "*" || terrain == "f" || terrain == "P")) { // Se não estiver a verificar na posição do meio (ou seja, está a verificar à volta) e uma das posições à volta tiver um destes caracteres, retorna false imediatamente
                return false
            }
        }
    }
    return true
}

fun isMovementPValid(currentCoord : Pair<Int, Int>, targetCoord : Pair<Int, Int>): Boolean { // verifica se está a mover apenas uma casa em todas as direções
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
            val valor = readText[0].toInt() - 49 // valor exato está com base na tabela ASCII
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
    val yleft = if (linha-1 !in 0 until numLines) linha else linha-1
    val xleft = if (coluna-1 !in 0 until numColumns) coluna else coluna-1
    val yright = if (linha+1 !in 0 until numLines) linha else linha+1
    val xright = if (coluna+1 !in 0 until numColumns) coluna else coluna+1

    return Pair(Pair(yleft,xleft),Pair(yright,xright))
}