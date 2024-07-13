package me.tbsten.tripletriad.shared.domain.game

data class Card(
    val id: Id,
    val top: CardNumber,
    val bottom: CardNumber,
    val left: CardNumber,
    val right: CardNumber,
) {
    class Id(val value: String)

    override fun equals(other: Any?): Boolean {
        if (other !is Card) return false
        return this.id == other.id
    }

    constructor(id: Id, allNumber: CardNumber) : this(
        id = id,
        top = allNumber,
        bottom = allNumber,
        left = allNumber,
        right = allNumber,
    )

    override fun toString(): String =
        "Card(t=${top.displayText},l=${left.displayText},r=${right.displayText},b=${bottom.displayText})"
}

sealed interface CardNumber {
    operator fun compareTo(other: CardNumber): Int
    val displayText: String

    data class Number(val number: Int) : CardNumber {
        override val displayText: String
            get() = "$number"

        override fun compareTo(other: CardNumber): Int =
            when (other) {
                is Number -> this.number - other.number
                is Ace -> -1
            }
    }

    data object Ace : CardNumber {
        override val displayText: String
            get() = "A"

        // otherがAceでない限り必ず勝つ
        override fun compareTo(other: CardNumber): Int =
            when (other) {
                is Ace -> 0
                else -> 1
            }
    }
}

