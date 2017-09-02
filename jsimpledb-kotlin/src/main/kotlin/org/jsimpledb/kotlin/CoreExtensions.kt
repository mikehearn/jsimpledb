package org.jsimpledb.kotlin

import org.jsimpledb.JObject
import org.jsimpledb.JSimpleDB
import org.jsimpledb.JTransaction
import org.jsimpledb.ValidationMode
import org.jsimpledb.kv.RetryTransactionException
import org.jsimpledb.tuple.Tuple2
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

/** Creates a new persistent instance of the class, within the current transaction. */
fun <T : JObject> KClass<T>.createPersistent(): T = JTransaction.getCurrent().create(this.java)

/** Returns a [NavigableSet] of all the persistent instances of this class. */
val <T : JObject> KClass<T>.allPersistent: NavigableSet<T> get() = JTransaction.getCurrent().getAll(this.java)

/**
 * A [NavigableMap] of the type of the property, to [NavigableSet]s of the matching object instances.
 * This index is live within the transaction.
 */
typealias PersistentFieldMap<FieldType, ClassType> = NavigableMap<FieldType, NavigableSet<ClassType>>

/**
 * A [NavigableSet] of pairs between the field values and the persistent objects themselves.
 * This index is live within the transaction.
 */
typealias PersistentFieldSet<FieldType, ClassType> = NavigableSet<Tuple2<FieldType, ClassType>>

/**
 * Returns a live (transactional) view of all the values of this property and objects that match, as a set of pairs.
 */
inline fun <reified T, reified R> KProperty1<T, R>.persistentIndexAsSet(): PersistentFieldSet<R, T> {
    return JTransaction.getCurrent().queryIndex(T::class.java, this.name, R::class.java).asSet()
}

/**
 * Returns a live (transactional) view of all the values of this property as a map of values to objects.
 */
inline fun <reified T, reified R> KProperty1<T, R>.persistentIndexAsMap(): PersistentFieldMap<R, T> {
    return JTransaction.getCurrent().queryIndex(T::class.java, this.name, R::class.java).asMap()
}

/**
 * Queries the indexed field for a match, returning it if found, or using [createPersistent] to instantiate a new
 * persistent object if not. Newly created instances have the property set to the given value automatically.
 *
 * The property that this is used on should be annotated as @JField(indexed = true, unique = true).
 */
inline fun <reified T : JObject, reified R> KMutableProperty1<T, R>.findByOrCreate(key: R): T {
    return findBy(key) ?: T::class.createPersistent().also { this.set(it, key) }
}

/**
 * Queries the indexed field for a match, returning it if found, or null if not found.
 *
 * The property that this is used on should be annotated as @JField(indexed = true, unique = true).
 */
inline fun <reified T : JObject, reified R> KMutableProperty1<T, R>.findBy(key: R): T? {
    return persistentIndexAsMap()[key]?.singleOrNull()
}

/**
 * Executes the given code in the context of a new transaction. Any existing transaction is put to one
 * side and restored afterwards. If a transaction retry is required, up to three attempts are used.
 */
fun <T> JSimpleDB.transaction(body: JTransaction.() -> T): T {
    val prev: JTransaction? = try { JTransaction.getCurrent() } catch (e: IllegalStateException) { null }
    var attempt = 0
    while (true) {
        val tx = createTransaction(true, ValidationMode.AUTOMATIC)
        JTransaction.setCurrent(tx)
        try {
            val result = tx.body()
            tx.commit()
            return result
        } catch (e: RetryTransactionException) {
            if (++attempt == 3)
                throw e
        } finally {
            tx.rollback()   // This has no effect if commit() was called successfully.
            JTransaction.setCurrent(prev)
        }
    }
}
