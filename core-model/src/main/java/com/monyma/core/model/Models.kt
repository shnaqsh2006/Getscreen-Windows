package com.monyma.core.model

import java.math.BigDecimal
import java.time.LocalDate

enum class TransactionType { INCOME, EXPENSE, TRANSFER }

enum class AccountType { CASH, BANK, CREDIT_CARD, SAVINGS, INVESTMENT, LOAN }

data class Account(
	val id: Long = 0L,
	val name: String,
	val type: AccountType,
	val openingBalance: BigDecimal = BigDecimal.ZERO,
	val currencyCode: String = "USD",
	val billingDayOfMonth: Int? = null,
	val paymentDueDayOfMonth: Int? = null
)

data class Category(
	val id: Long = 0L,
	val name: String,
	val parentCategoryId: Long? = null,
	val isIncome: Boolean
)

data class Transaction(
	val id: Long = 0L,
	val type: TransactionType,
	val accountId: Long,
	val amount: BigDecimal,
	val date: LocalDate,
	val categoryId: Long?,
	val note: String? = null,
	val transferAccountId: Long? = null
)