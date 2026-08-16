package com.rombsquare.solocards.domain.usecases.serializer

data class SerializerUseCases(
    val serializeQuiz: SerializeQuiz,
    val deserializeQuiz: DeserializeQuiz,
    val serializeProgress: SerializeProgress,
    val deserializeProgress: DeserializeProgress
)
