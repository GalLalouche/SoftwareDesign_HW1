package il.ac.technion.cs.softwaredesign.storage

interface ISecureStorageKey<Key> :  Comparable<Key> ,IStorageConvertable<Key> {
    fun<P : IStorageConvertable<P>> getPointer() : IStorageConvertable<P>
}