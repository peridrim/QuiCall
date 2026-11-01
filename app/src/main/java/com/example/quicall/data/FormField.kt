package com.example.quicall.data

enum class FormField(val isRequired: Boolean = false) {
    NAME(true),
    LAST_NAME,
    PHONE_NUMBER(true),
    EMAIL,
    UNSPECIFIED
}