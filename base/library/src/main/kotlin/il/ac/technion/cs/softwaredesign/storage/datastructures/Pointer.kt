package il.ac.technion.cs.softwaredesign.storage.datastructures

import il.ac.technion.cs.softwaredesign.storage.utils.ConversionUtils
import il.ac.technion.cs.softwaredesign.storage.ISequenceGenerator
import javax.inject.Inject

class Pointer : IPointer {
    override fun getValue(): Long {
        return pointer
    }
//    @Inject
//    private lateinit var generator: ISequenceGenerator
//    init {
//        pointer=generator.next()
//    }

    private var pointer : Long = 0

    //copy ctor
    constructor(p : Long) {
        this.pointer=p
    }

    override fun toByteArray():ByteArray = ConversionUtils.longToBytes(pointer)

    override fun fromByteArray(value:ByteArray) {
        //if (value == null) throw IllegalArgumentException("Pointer can not be null")
        pointer = ConversionUtils.bytesToLong(value)
    }
}