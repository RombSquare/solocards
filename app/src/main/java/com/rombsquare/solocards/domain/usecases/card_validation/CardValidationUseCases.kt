package com.rombsquare.solocards.domain.usecases.card_validation

data class CardValidationUseCases(
    val basicCardValidation: BasicCardValidation,
    val booleanModeValidation: BooleanModeValidation,
    val editCardValidation: EditCardValidation,
    val mixedModeValidation: MixedModeValidation,
    val optionModeValidation: OptionModeValidation,
    val cardTextValidation: CardTextValidation
)