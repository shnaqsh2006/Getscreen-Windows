package com.monyma.core.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.monyma.core.model.AccountType
import com.monyma.core.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDate

@Entity(tableName = "accounts")
data class AccountEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	@ColumnInfo(name = "name") val name: String,
	@ColumnInfo(name = "type") val type: AccountType,
	@ColumnInfo(name = "opening_balance") val openingBalance: BigDecimal,
	@ColumnInfo(name = "currency_code") val currencyCode: String,
	@ColumnInfo(name = "billing_day") val billingDayOfMonth: Int?,
	@ColumnInfo(name = "payment_due_day") val paymentDueDayOfMonth: Int?
)

@Entity(
	tableName = "categories",
	indices = [Index("parent_id")]
)
data class CategoryEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	@ColumnInfo(name = "name") val name: String,
	@ColumnInfo(name = "parent_id") val parentCategoryId: Long?,
	@ColumnInfo(name = "is_income") val isIncome: Boolean
)

@Entity(
	tableName = "transactions",
	foreignKeys = [
		ForeignKey(
			entity = AccountEntity::class,
			parentColumns = ["id"],
			childColumns = ["account_id"],
			onDelete = ForeignKey.CASCADE
		),
		ForeignKey(
			entity = AccountEntity::class,
			parentColumns = ["id"],
			childColumns = ["transfer_account_id"],
			onDelete = ForeignKey.SET_NULL
		),
		ForeignKey(
			entity = CategoryEntity::class,
			parentColumns = ["id"],
			childColumns = ["category_id"],
			onDelete = ForeignKey.SET_NULL
		)
	],
	indices = [Index("account_id"), Index("category_id"), Index("transfer_account_id" )]
)
data class TransactionEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	@ColumnInfo(name = "type") val type: TransactionType,
	@ColumnInfo(name = "account_id") val accountId: Long,
	@ColumnInfo(name = "amount") val amount: BigDecimal,
	@ColumnInfo(name = "date") val date: LocalDate,
	@ColumnInfo(name = "category_id") val categoryId: Long?,
	@ColumnInfo(name = "note") val note: String?,
	@ColumnInfo(name = "transfer_account_id") val transferAccountId: Long?
)

class Converters {
	@TypeConverter
	fun bigDecimalToString(value: BigDecimal?): String? = value?.toPlainString()
	@TypeConverter
	fun stringToBigDecimal(value: String?): BigDecimal? = value?.let { BigDecimal(it) }

	@TypeConverter
	fun localDateToString(value: LocalDate?): String? = value?.toString()
	@TypeConverter
	fun stringToLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }
}

@Dao
interface AccountDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(account: AccountEntity): Long
	@Query("SELECT * FROM accounts ORDER BY name")
	suspend fun getAll(): List<AccountEntity>
	@Delete
	suspend fun delete(account: AccountEntity)
}

@Dao
interface CategoryDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(category: CategoryEntity): Long
	@Query("SELECT * FROM categories ORDER BY name")
	suspend fun getAll(): List<CategoryEntity>
	@Delete
	suspend fun delete(category: CategoryEntity)
}

@Dao
interface TransactionDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(tx: TransactionEntity): Long
	@Query("SELECT * FROM transactions WHERE date BETWEEN :from AND :to ORDER BY date DESC, id DESC")
	suspend fun getInRange(from: LocalDate, to: LocalDate): List<TransactionEntity>
	@Query("SELECT * FROM transactions ORDER BY date DESC, id DESC LIMIT :limit")
	suspend fun getRecent(limit: Int): List<TransactionEntity>
	@Delete
	suspend fun delete(tx: TransactionEntity)
}

@Database(
	entities = [AccountEntity::class, CategoryEntity::class, TransactionEntity::class],
	version = 1,
	exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MonymaDatabase : RoomDatabase() {
	abstract fun accountDao(): AccountDao
	abstract fun categoryDao(): CategoryDao
	abstract fun transactionDao(): TransactionDao
}