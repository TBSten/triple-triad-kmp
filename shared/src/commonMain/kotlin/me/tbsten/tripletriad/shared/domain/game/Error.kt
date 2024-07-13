package me.tbsten.tripletriad.shared.domain.game

sealed class AppException(override val message: String?) : Exception()
