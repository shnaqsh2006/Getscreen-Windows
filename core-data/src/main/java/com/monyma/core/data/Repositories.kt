package com.monyma.core.data

import android.content.Context
import androidx.room.Room
import com.monyma.core.database.AccountDao
import com.monyma.core.database.AccountEntity
import com.monyma.core.database.CategoryDao
import com.monyma.core.database.CategoryEntity
import com.monyma.core.database.MonymaDatabase
import com.monyma.core.database.TransactionDao
import com.monyma.core.database.TransactionEntity
import com.monyma.core.model.Account
import com.monyma.core.model.Category
import com.monyma.core.model.Transaction
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface AccountsRepository {
	suspend fun upsert(account: Account): Long
	suspend fun getAll(): List<Account>
	suspend fun delete(id: Long)
}

interface CategoriesRepository {
	suspend fun upsert(category: Category): Long
	suspend fun getAll(): List<Category>
	suspend fun delete(id: Long)
}

interface TransactionsRepository {
	suspend fun upsert(tx: Transaction): Long
	suspend fun getRecent(limit: Int): List<Transaction>
	suspend fun delete(id: Long)
}

@Singleton
class OfflineAccountsRepository @Inject constructor(
	private val dao: AccountDao
) : AccountsRepository {
	override suspend fun upsert(account: Account): Long = withContext(Dispatchers.IO) {
		dao.upsert(
			AccountEntity(
				id = account.id,
				name = account.name,
				type = account.type,
				openingBalance = account.openingBalance,
				currencyCode = account.currencyCode,
				billingDayOfMonth = account.billingDayOfMonth,
				paymentDueDayOfMonth = account.paymentDueDayOfMonth
			)
		)
	}

	override suspend fun getAll(): List<Account> = withContext(Dispatchers.IO) {
		dao.getAll().map {
			Account(
				id = it.id,
				name = it.name,
				type = it.type,
				openingBalance = it.openingBalance,
				currencyCode = it.currencyCode,
				billingDayOfMonth = it.billingDayOfMonth,
				paymentDueDayOfMonth = it.paymentDueDayOfMonth
			)
		}
	}

	override suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
		dao.delete(AccountEntity(id = id, name = "", type = com.monyma.core.model.AccountType.CASH, openingBalance = java.math.BigDecimal.ZERO, currencyCode = "", billingDayOfMonth = null, paymentDueDayOfMonth = null))
	}
}

@Singleton
class OfflineCategoriesRepository @Inject constructor(
	private val dao: CategoryDao
) : CategoriesRepository {
	override suspend fun upsert(category: Category): Long = withContext(Dispatchers.IO) {
		dao.upsert(
			CategoryEntity(
				id = category.id,
				name = category.name,
				parentCategoryId = category.parentCategoryId,
				isIncome = category.isIncome
			)
		)
	}

	override suspend fun getAll(): List<Category> = withContext(Dispatchers.IO) {
		dao.getAll().map {
			Category(
				id = it.id,
				name = it.name,
				parentCategoryId = it.parentCategoryId,
				isIncome = it.isIncome
			)
		}
	}

	override suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
		dao.delete(CategoryEntity(id = id, name = "", parentCategoryId = null, isIncome = false))
	}
}

@Singleton
class OfflineTransactionsRepository @Inject constructor(
	private val dao: TransactionDao
) : TransactionsRepository {
	override suspend fun upsert(tx: Transaction): Long = withContext(Dispatchers.IO) {
		dao.upsert(
			TransactionEntity(
				id = tx.id,
				type = tx.type,
				accountId = tx.accountId,
				amount = tx.amount,
				date = tx.date,
				categoryId = tx.categoryId,
				note = tx.note,
				transferAccountId = tx.transferAccountId
			)
		)
	}

	override suspend fun getRecent(limit: Int): List<Transaction> = withContext(Dispatchers.IO) {
		dao.getRecent(limit).map {
			Transaction(
				id = it.id,
				type = it.type,
				accountId = it.accountId,
				amount = it.amount,
				date = it.date,
				categoryId = it.categoryId,
				note = it.note,
				transferAccountId = it.transferAccountId
			)
		}
	}

	override suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
		dao.delete(TransactionEntity(id = id, type = com.monyma.core.model.TransactionType.EXPENSE, accountId = 0, amount = java.math.BigDecimal.ZERO, date = java.time.LocalDate.now(), categoryId = null, note = null, transferAccountId = null))
	}
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
	@Provides
	@Singleton
	fun provideDatabase(@ApplicationContext appContext: Context): MonymaDatabase =
		Room.databaseBuilder(appContext, MonymaDatabase::class.java, "monyma.db").build()

	@Provides fun provideAccountDao(db: MonymaDatabase): AccountDao = db.accountDao()
	@Provides fun provideCategoryDao(db: MonymaDatabase): CategoryDao = db.categoryDao()
	@Provides fun provideTransactionDao(db: MonymaDatabase): TransactionDao = db.transactionDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
	@Binds abstract fun bindAccountsRepository(impl: OfflineAccountsRepository): AccountsRepository
	@Binds abstract fun bindCategoriesRepository(impl: OfflineCategoriesRepository): CategoriesRepository
	@Binds abstract fun bindTransactionsRepository(impl: OfflineTransactionsRepository): TransactionsRepository
}