package il.ac.technion.cs.softwaredesign.storage.datastructures

import il.ac.technion.cs.softwaredesign.storage.IStorageConverter

interface IPointer : IStorageConverter<IPointer> //TODO: should we have a dereference here maybe