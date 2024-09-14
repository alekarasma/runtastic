package com.asmaa.core.domain.util

sealed interface Result<out D, out E : Error> {
    data class Success<D>(val data: D) : Result<D, Nothing>
    data class Error<E : com.asmaa.core.domain.util.Error>(val error: E) : Result<Nothing, E>
}

/**
 * Covariant - Covariance allows type to preserve its inheritance relationship. So for example if Type A is subtype of B.
 * and a type C is covariant in its generic type parameter T. then C<A> is subtype of C<B>
 * Use Covariant for Producer, when you produce generic types and do not consume it
 * Example Fruit Producer
 * val covariantProducer: Producer<Fruit> = appleProducer
 *
 * Invariant - Invariance does not allow type to preserve its inheritance relationship. So for example
 * if Type A is subtype of B and C is invariant in its generic type parameter T, then C<A> is not subtype of C<B>
 * Invariance: Use when you both consume and produce values of the generic type T.
 * This is typical for classes like containers or collections where you might need to both read and write T.
 * val invariantBox: Box<Fruit> = appleBox // This will not compile because Box is invariant
 *
 * Contravariance - Contravariance allows type to inverse its inheritance relationship. So for example
 * if Type A is subtype of B and C is contravariant in its generic type parameter T, then C<B> is subtype of C<A>.
 * Use when you are Consumer
 * val contravariantConsumer: Consumer<Apple> = fruitConsumer
 */
inline fun <T, E : Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Success -> Result.Success(map(data))
        is Result.Error -> this
    }

}

fun <T, E : Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {}
}

typealias EmptyResult<E> = Result<Unit, E>