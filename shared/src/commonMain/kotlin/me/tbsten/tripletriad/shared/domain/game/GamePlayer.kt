package me.tbsten.tripletriad.shared.domain.game

import kotlin.jvm.JvmInline

class GamePlayer(
    val id: Id,
    val name: String,
    val hands: Hands,
) {
    @JvmInline
    value class Id(val value: String)

    override fun equals(other: Any?): Boolean =
        other is GamePlayer &&
                this.id == other.id

    fun updateHands(hands: Hands) =
        GamePlayer(
            id = this.id,
            name = this.name,
            hands = hands,
        )
}

typealias Hands = List<Card>

