package il.ac.technion.cs.softwaredesign.storage.datastructures

import il.ac.technion.cs.softwaredesign.storage.utils.ConversionUtils
import il.ac.technion.cs.softwaredesign.storage.ISequenceGenerator
import javax.inject.Inject


class Pointer() : IPointer {
    @Inject
    private lateinit var generator: ISequenceGenerator
    private var pointer : Long
    init {
        pointer=generator.next()
    }
    //copy ctor
    constructor(p : Long) : this() {
        this.pointer=p
    }

    override fun toByteArray():ByteArray = ConversionUtils.longToBytes(pointer)

    override fun fromByteArray(value:ByteArray?) {
        if (value == null) throw IllegalArgumentException("Pointer can not be null")
        pointer = ConversionUtils.bytesToLong(value)
    }
}