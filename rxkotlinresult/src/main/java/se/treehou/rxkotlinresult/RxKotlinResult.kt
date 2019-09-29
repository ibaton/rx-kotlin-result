package se.treehou.rxkotlinresult

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import se.treehou.kotlinresult.Result
import se.treehou.kotlinresult.Result.Failure
import se.treehou.kotlinresult.Result.Success

fun <T> Single<T>.wrapResult(): Single<Result<T>> {
    return map<Result<T>> { value -> Success(value) }
            .onErrorReturn { Failure(it) }
}

fun <T> Flowable<T>.wrapResult(): Flowable<Result<T>> {
    return map<Result<T>> { value -> Success(value) }
            .onErrorReturn { Failure(it) }
}

fun <T> Observable<T>.wrapResult(): Observable<Result<T>> {
    return map<Result<T>> { value -> Success(value) }
            .onErrorReturn { Failure(it) }
}

fun <T> Flowable<Result<T>>.filterSuccess(): Flowable<T> {
    return filter {
        when (it) {
            is Result.Success -> true
            is Result.Failure -> false
        }
    }.map { (value, _) -> value }
}

fun <T> Observable<Result<T>>.filterSuccess(): Observable<T> {
    return filter {
        when (it) {
            is Result.Success -> true
            is Result.Failure -> false
        }
    }.map { (value, _) -> value }
}

fun <T, D> Single<Result<T>>.mapResult(action: (T) -> D): Single<Result<D>> {
    return map { result ->
        when(result) {
            is Result.Success -> Result.Success(action(result.value))
            is Result.Failure -> Result.Failure<D>(result.throwable)
        }
    }
}

fun <T, D> Flowable<Result<T>>.mapResult(action: (T) -> D): Flowable<Result<D>> {
    return map { result ->
        when(result) {
            is Result.Success -> Result.Success(action(result.value))
            is Result.Failure -> Result.Failure<D>(result.throwable)
        }
    }
}

fun <T, D> Observable<Result<T>>.mapResult(action: (T) -> D): Observable<Result<D>> {
    return map { result ->
        when(result) {
            is Result.Success -> Result.Success(action(result.value))
            is Result.Failure -> Result.Failure<D>(result.throwable)
        }
    }
}

fun <T, D> Flowable<Result<T>>.switchMapResult(action: (T) -> Flowable<D>): Flowable<Result<D>> {
    return switchMap { result ->
        when(result) {
            is Result.Success -> action(result.value).map { Result.Success(it) }
            is Result.Failure -> Flowable.just(Result.Failure<D>(result.throwable))
        }
    }
}