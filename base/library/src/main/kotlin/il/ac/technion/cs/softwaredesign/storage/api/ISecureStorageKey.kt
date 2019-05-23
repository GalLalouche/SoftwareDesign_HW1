package il.ac.technion.cs.softwaredesign.storage

interface ISecureStorageKey<Key> :  Comparable<Key> ,IStorageConvertable<Key> {
}