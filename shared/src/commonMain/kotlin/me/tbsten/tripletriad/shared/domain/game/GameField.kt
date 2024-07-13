package me.tbsten.tripletriad.shared.domain.game

data class GameField(
    val squares: List<Square>,
    val width: Int,
    val height: Int,
) {
    val isFill: Boolean
        get() = squares.find { it is Square.Open } == null

    init {
        require(squares.size == width * height)
    }

    operator fun get(x: Int, y: Int): Square =
        squares[squareOffsetToIndex(SquareOffset(x, y))]

    operator fun get(offset: SquareOffset): Square =
        this[offset.x, offset.y]

    fun getOrNull(x: Int, y: Int): Square? =
        if (
            squareOffsetToIndex(
                SquareOffset(x, y)
            ) in 0 until (width * height)
        ) {
            this[x, y]
        } else {
            null
        }

    fun getOrNull(offset: SquareOffset): Square? =
        this.getOrNull(offset.x, offset.y)

    fun squareOffsetToIndex(squareOffset: SquareOffset) =
        squareOffset.y * width + squareOffset.x

    fun topOf(squareOffset: SquareOffset): SquareOffset? =
        if (0 <= squareOffset.y - 1) {
            SquareOffset(
                x = squareOffset.x,
                y = squareOffset.y - 1,
            )
        } else {
            null
        }

    fun bottomOf(squareOffset: SquareOffset): SquareOffset? =
        if (squareOffset.y + 1 < height) {
            SquareOffset(
                x = squareOffset.x,
                y = squareOffset.y + 1,
            )
        } else {
            null
        }

    fun leftOf(squareOffset: SquareOffset): SquareOffset? =
        if (0 <= squareOffset.x - 1) {
            SquareOffset(
                x = squareOffset.x - 1,
                y = squareOffset.y,
            )
        } else {
            null
        }

    fun rightOf(squareOffset: SquareOffset): SquareOffset? =
        if (squareOffset.x + 1 < width) {
            SquareOffset(
                x = squareOffset.x + 1,
                y = squareOffset.y,
            )
        } else {
            null
        }

    fun topCardOf(squareOffset: SquareOffset): Card? =
        topOf(squareOffset)?.let { this[it].card }

    fun bottomCardOf(squareOffset: SquareOffset): Card? =
        bottomOf(squareOffset)?.let { this[it].card }

    fun leftCardOf(squareOffset: SquareOffset): Card? =
        leftOf(squareOffset)?.let { this[it].card }

    fun rightCardOf(squareOffset: SquareOffset): Card? =
        rightOf(squareOffset)?.let { this[it].card }

    override fun toString(): String =
        """
            GameField(${
            squares.chunked(width).joinToString(", ") {
                "[${
                    it.joinToString(",") {
                        it.card?.let { "(t=${it.top.displayText},b=${it.bottom.displayText},l=${it.left.displayText},r=${it.right.displayText})" } ?: "null"
                    }
                }]"
            }
        })
        """.trimIndent()
}

sealed interface Square {
    val card: Card?

    data class Owned(val owner: GamePlayer, override val card: Card) : Square

    // selectedかどうかの判定などで同じと判定されないようにするため data objectにはしない
    class Open : Square {
        override val card: Card? = null
        override fun toString(): String =
            "Square.Open"
    }
}

data class SquareOffset(val x: Int, val y: Int)
