package com.aoverin.invest.exceptions

sealed class InvestException(message: String, cause: Throwable?) : RuntimeException(message, cause)

class RequestApiBlockingException(message: String, cause: Throwable?) : InvestException(message, cause)

class RequestApiNotFoundException(message: String, cause: Throwable?) : InvestException(message, cause)
