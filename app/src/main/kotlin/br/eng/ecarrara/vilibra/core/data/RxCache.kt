package br.eng.ecarrara.vilibra.core.data

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Generic cache interface definition.
 */
interface RxCache<Key, Type> {

    operator fun get(elementKey: Key): Maybe<Type>
    fun put(elementKey: Key, elementToBeCached: Type): Completable
    fun remove(elementKeyToBeRemoved: Key): Completable
    fun isCached(elementKey: Key): Single<Boolean>
    fun clear(): Completable

}