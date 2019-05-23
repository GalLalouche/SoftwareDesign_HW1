package il.ac.technion.cs.softwaredesign.storage.api

interface ISecureStorageKey<Key> :  Comparable<Key> , IStorageConvertable<Key>