package com.rombsquare.solocards.features.menu_feature.domain.usecases.serializer_usecases

data class SerializerUseCases(
    val serializeQuiz: SerializeQuiz,
    val deserializeQuiz: DeserializeQuiz,
    val serializeProgress: SerializeProgress,
    val deserializeProgress: DeserializeProgress
)
