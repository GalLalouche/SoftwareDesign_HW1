package il.ac.technion.cs.softwaredesign.storage.statistics

import il.ac.technion.cs.softwaredesign.storage.IStorageConvertable

interface IStatisticsStorage {
    fun<T:IStorageConvertable<T>> getValue(key : String) : T?
    fun<T:IStorageConvertable<T>> setValue(key : String, value : T)
}