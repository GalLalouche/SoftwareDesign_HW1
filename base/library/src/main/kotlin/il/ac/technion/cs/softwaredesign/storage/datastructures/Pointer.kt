package il.ac.technion.cs.softwaredesign.storage.datastructures

import il.ac.technion.cs.softwaredesign.storage.utils.ByteUtils
import javax.inject.Inject


class Pointer() : IPointer {
    @Inject
    private lateinit var generator:ISequenceGenerator
    private var pointer : Long
    init {
        pointer=generator.next()
    }
    //copy ctor
    constructor(p : Long) : this() {
        this.pointer=p
    }

    override fun toByteArray():ByteArray = ByteUtils.longToBytes(pointer)

    override fun fromByteArray(value:ByteArray?) {
        if (value == null) throw IllegalArgumentException("Pointer can not be null")
        pointer = ByteUtils.bytesToLong(value)
    }
}