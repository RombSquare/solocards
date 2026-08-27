package com.rombsquare.solocards.core.domain.usecases.validation.card_validation

data class CardValidationUseCases(
    val basicCardValidation: BasicCardValidation,
    val booleanModeValidation: BooleanModeValidation,
    val mixedModeValidation: MixedModeValidation,
    val optionModeValidation: OptionModeValidation,
    val cardTextValidation: CardTextValidation
)