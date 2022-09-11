package connectfour

var lastPlayedRow = -1
var numberOfGames = -1
var currentGameNbr = 1
var firstPlayerPoints = 0
var secondPlayerPoints = 0
var playerNames = ""
var boardSize = ""
var gameOver = false
var currentPlayer = ""

fun main() {
    println("Connect Four")
    println("First player's name:")
    val firstPlayer = readln()
    println("Second player's name:")
    val secondPlayer = readln()
    game(firstPlayer, secondPlayer)

}

private fun gameConfig() {
    println(
        "Do you want to play single or multiple games?\n" +
                "For a single game, input 1 or press Enter\n" +
                "Input a number of games:"
    )
    val userInput = readln()
    if (userInput.isBlank()) {
        numberOfGames = 1
    } else if (isNumeric(userInput) && userInput.toInt() > 0) {
        numberOfGames = userInput.toInt()
    } else {
        println("Invalid input")
        gameConfig()
    }
}

private fun game(firstPlayer: String, secondPlayer: String) {
    val dimensions = dealWithPrintingTheSize(firstPlayer, secondPlayer)
    val rows = dimensions[0]
    val columns = dimensions[1]
    gameConfig()
    println(playerNames)
    println(boardSize)
    displayNumberOfGamesInfo()
    playGame(firstPlayer, secondPlayer, rows, columns)
}

fun playGame(firstPlayer: String, secondPlayer: String, rows: Int, columns: Int) {
    currentPlayer = firstPlayer
    while (numberOfGames > 0) {
        displayInitialBoard(columns, rows)
        val board = getInitializeBoard(rows, columns)
        var gameFinished = false
        while (!gameFinished) {
            gameFinished = playTurn(board, secondPlayer, columns, currentPlayer)
            currentPlayer = changeCurrentPlayer(firstPlayer, secondPlayer, currentPlayer)
        }
        if (!gameOver) {
            printlnScore(firstPlayer, secondPlayer)
        }
        lastPlayedRow = -1
        numberOfGames--
        currentGameNbr++
    }
    println("Game Over!")

}

fun printlnScore(firstPlayer: String, secondPlayer: String) {
    println(
        "Score\n" +
                "$firstPlayer: $firstPlayerPoints $secondPlayer: $secondPlayerPoints"
    )
}

fun displayNumberOfGamesInfo() {
    if (numberOfGames == 1) {
        println("Single game")
    } else {
        println("Total $numberOfGames games")
    }
}

fun gameNumberInfo() {
    if (numberOfGames != 1 || currentGameNbr > 2) {
        println("Game #$currentGameNbr")

    }
}

fun playTurn(
    board: MutableList<MutableList<String>>,
    secondPlayer: String,
    columns: Int,
    currentPlayer: String,
): Boolean {
    var lastMove = -1
    var playerPlayed = false;
    while (!playerPlayed) {
        println("$currentPlayer's turn:")
        val input = readln()
        if (input == "end") {
            gameOver = true
            return true
        } else if (!isNumeric(input)) {
            println("Incorrect column number")
            println("$currentPlayer's turn:")
        } else if (input.toInt() < 1 || input.toInt() > columns) {
            println("The column number is out of range (1 - $columns)")
            println("$currentPlayer's turn:")
        } else if (isFull(board, input.toInt() - 1)) {
            println("Column $input is full")
            println("$currentPlayer's turn:")
        } else {
            doTheMove(board, input.toInt(), secondPlayer, currentPlayer)
            lastMove = input.toInt()
            playerPlayed = true
        }
    }
    return checkIfFinished(board, currentPlayer, secondPlayer, lastMove)
}

fun checkIfFinished(
    board: MutableList<MutableList<String>>,
    currentPlayer: String,
    secondPlayer: String,
    lastMove: Int
): Boolean {
    if (winnerExist(board, secondPlayer, currentPlayer, lastMove)) {
        return true
    }

    if (boardIsFull(board)) {
        println("It is a draw")
        firstPlayerPoints++
        secondPlayerPoints++
        return true
    }
    return false
}

fun winnerExist(
    board: MutableList<MutableList<String>>,
    secondPlayer: String,
    currentPlayer: String,
    lastMove: Int
): Boolean {
    var symbol = "o"
    if (currentPlayer == secondPlayer) {
        symbol = "*"
    }
    if (winnerRow(board, symbol) || winnerColumn(board, symbol) || winnerDiagonal(board, symbol, lastMove)) {
        println("Player $currentPlayer won")
        if (currentPlayer == secondPlayer) {
            secondPlayerPoints += 2
        } else {
            firstPlayerPoints += 2
        }
        return true
    }
    return false
}

fun winnerDiagonal(board: MutableList<MutableList<String>>, symbol: String, lastMove: Int): Boolean {

    if (isRightDiagonalWinner(board, symbol, lastPlayedRow, lastMove - 1)) {
        return true
    }
    if (isLeftDiagonalWinner(board, symbol, lastPlayedRow, lastMove - 1)) {
        return true
    }
    return false
}

fun isRightDiagonalWinner(board: MutableList<MutableList<String>>, symbol: String, row: Int, column: Int): Boolean {
    val diagonalLane = mutableListOf(board[row][column])

    if (row - 1 in 0 until board.size && column + 1 in 0 until board[0].size) {
        diagonalLane.add(board[row - 1][column + 1])
    }
    if (row - 2 in 0 until board.size && column + 2 in 0 until board[0].size) {
        diagonalLane.add(board[row - 2][column + 2])
    }
    if (row - 3 in 0 until board.size && column + 3 in 0 until board[0].size) {
        diagonalLane.add(board[row - 3][column + 3])
    }
    if (row + 1 in 0 until board.size && column - 1 in 0 until board[0].size) {
        diagonalLane.add(board[row + 1][column - 1])
    }
    if (row + 2 in 0 until board.size && column - 2 in 0 until board[0].size) {
        diagonalLane.add(board[row + 2][column - 2])
    }
    if (row + 3 in 0 until board.size && column - 3 in 0 until board[0].size) {
        diagonalLane.add(board[row + 3][column - 3])
    }

    var counter = 0
    for (element in diagonalLane) {
        if (element == symbol) {
            counter++
        } else {
            counter = 0
        }
        if (counter == 4) {
            return true
        }
    }

    return false
}

fun isLeftDiagonalWinner(board: MutableList<MutableList<String>>, symbol: String, row: Int, column: Int): Boolean {
    val diagonalLane = mutableListOf(board[row][column])

    if (row + 1 in 0 until board.size && column + 1 in 0 until board[0].size) {
        diagonalLane.add(board[row + 1][column + 1])
    }
    if (row + 2 in 0 until board.size && column + 2 in 0 until board[0].size) {
        diagonalLane.add(board[row + 2][column + 2])
    }
    if (row + 3 in 0 until board.size && column + 3 in 0 until board[0].size) {
        diagonalLane.add(board[row + 3][column + 3])
    }
    if (row - 1 in 0 until board.size && column - 1 in 0 until board[0].size) {
        diagonalLane.add(board[row - 1][column - 1])
    }
    if (row - 2 in 0 until board.size && column - 2 in 0 until board[0].size) {
        diagonalLane.add(board[row - 2][column - 2])
    }
    if (row - 3 in 0 until board.size && column - 3 in 0 until board[0].size) {
        diagonalLane.add(board[row - 3][column - 3])
    }

    var counter = 0
    for (element in diagonalLane) {
        if (element == symbol) {
            counter++
        } else {
            counter = 0
        }
        if (counter == 4) {
            return true
        }
    }

    return false
}

fun winnerColumn(board: MutableList<MutableList<String>>, symbol: String): Boolean {
    var counter = 0
    for (columnIndex in 0 until board[0].size) {
        for (rowIndex in 0 until board.size) {
            if (board[rowIndex][columnIndex] == symbol) {
                counter++
            } else {
                counter = 0
            }
            if (counter == 4) {
                return true
            }
        }
        counter = 0
    }
    return false
}

fun winnerRow(board: MutableList<MutableList<String>>, symbol: String): Boolean {
    for (lane in board) {
        if (winnerLane(lane, symbol)) {
            return true
        }
    }
    return false
}

fun winnerLane(lane: MutableList<String>, symbol: String): Boolean {
    var counter = 0
    for (element in lane) {
        if (element == symbol) {
            counter++
        } else {
            counter = 0
        }
        if (counter == 4) {
            return true
        }
    }
    return false
}

fun boardIsFull(board: MutableList<MutableList<String>>): Boolean {
    for (list in board) {
        for (element in list) {
            if (element == " ") {
                return false
            }
        }
    }
    return true
}

fun changeCurrentPlayer(
    firstPlayer: String,
    secondPlayer: String,
    currentPlayer: String
): String {
    return if (currentPlayer == firstPlayer) {
        secondPlayer
    } else {
        firstPlayer
    }
}

fun doTheMove(
    board: MutableList<MutableList<String>>,
    inputNumber: Int,
    secondPlayer: String,
    currentPlayer: String
) {
    val changeRow = findRow(board, inputNumber)
    var char = "o"
    if (currentPlayer == secondPlayer) {
        char = "*"
    }
    board[changeRow][inputNumber - 1] = char
    displayBoard(board)
}

fun findRow(board: MutableList<MutableList<String>>, columnNumber: Int): Int {
    for (i in board.size - 1 downTo 0) {
        if (board[i][columnNumber - 1] == " ") {
            lastPlayedRow = i
            return i
        }
    }
    return -1
}

fun isFull(board: MutableList<MutableList<String>>, move: Int): Boolean {
    return board[0][move] != " "
}

fun getInitializeBoard(rows: Int, columns: Int): MutableList<MutableList<String>> {
    val board = mutableListOf<MutableList<String>>()
    for (number in 0 until rows) {
        board.add(getInitializeBoardLane(columns))
    }
    return board
}

fun getInitializeBoardLane(columns: Int): MutableList<String> {
    val lane = mutableListOf<String>()
    for (number in 0 until columns) {
        lane.add(" ")
    }
    return lane
}


private fun displayInitialBoard(columns: Int, rows: Int) {
    gameNumberInfo()
    printlnFirstLine(columns)
    printlnMiddleLines(rows, columns)
    printlnLastLine(columns)
}

private fun displayBoard(board: MutableList<MutableList<String>>) {
    printlnFirstLine(board[0].size)
    printlnMiddleLines(board)
    printlnLastLine(board[0].size)
}

fun printlnMiddleLines(board: MutableList<MutableList<String>>) {
    for (i in 0 until board.size) {
        printMiddleRow(board, i)
    }
}

fun printMiddleRow(board: MutableList<MutableList<String>>, rowIndex: Int) {
    var output = "║";
    for (i in 0 until board[0].size) {
        var value = board[rowIndex][i]
        output += ("$value║")
    }
    println(output)
}

fun printlnFirstLine(columns: Int) {
    var output = " ";
    for (i in 1..columns) {
        output += ("$i ")
    }
    println(output)
}

fun printlnMiddleLines(rows: Int, columns: Int) {
    for (i in 1..rows) {
        printMiddleRow(columns)
    }
}

fun printMiddleRow(columns: Int) {
    var output = "║";
    for (i in 1..columns) {
        output += (" ║")
    }
    println(output)
}

fun printlnLastLine(columns: Int) {
    var output = "╚";
    for (i in 1 until columns) {
        output += ("═╩")
    }
    output += "═╝"
    println(output)
}

private fun dealWithPrintingTheSize(firstPlayer: String, secondPlayer: String): Array<Int> {
    while (true) {
        println(
            "Set the board dimensions (Rows x Columns)\n" +
                    "Press Enter for default (6 x 7)"
        )
        var boardInput = readln()

        boardInput = boardInput.replace("\\s".toRegex(), "")

        if (boardInput.isBlank()) {
            playerNames = "$firstPlayer VS $secondPlayer";
            boardSize = "6 X 7 board";
            return arrayOf(6, 7)

        }
        var rows = "b"
        var columns = "a"
        if (boardInput.contains("x")) {
            rows = boardInput.split("x")[0]
            columns = boardInput.split("x")[1]
        } else if (boardInput.contains("X")) {
            rows = boardInput.split("X")[0]
            columns = boardInput.split("X")[1]
        }
        if (valuesOfRowsAndColumnsAreOk(rows, columns)) {
            playerNames = "$firstPlayer VS $secondPlayer"
            boardSize = "$rows X $columns board"
            return arrayOf(rows.toInt(), columns.toInt())
        }
    }
}

fun valuesOfRowsAndColumnsAreOk(rows: String, columns: String): Boolean {
    if (!isNumeric(rows) || !isNumeric(columns)) {
        println("Invalid input")
        return false
    } else {
        val rowsInt = rows.toInt()
        val columnsInt = columns.toInt()
        if (!isNumberInRange(rowsInt)) {
            println("Board rows should be from 5 to 9")
            return false
        }
        if (!isNumberInRange(columnsInt)) {
            println("Board columns should be from 5 to 9")
            return false
        }
    }
    return true
}

fun isNumeric(toCheck: String): Boolean {
    val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
    return toCheck.matches(regex)
}

fun isNumberInRange(number: Int): Boolean {
    return number in 5..9
}