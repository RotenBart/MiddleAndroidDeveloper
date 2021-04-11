package ru.skillbranch.kotlinexample.extensions

fun <T> List<T>.dropLastUntil(predicate: (T) -> Boolean): List<T> {
    val droppedList = mutableListOf<T>()
    this.forEach {
        if(predicate(it)) {
            droppedList.addAll(this.subList(0, this.indexOf(it)))
        }
    }
    return droppedList
}