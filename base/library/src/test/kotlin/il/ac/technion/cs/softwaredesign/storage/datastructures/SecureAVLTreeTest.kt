package il.ac.technion.cs.softwaredesign.storage.datastructures

import com.google.common.primitives.Longs
import il.ac.technion.cs.softwaredesign.storage.ISecureStorageKey
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.utils.TREE_CONST.ROOT_INIT_INDEX
import il.ac.technion.cs.softwaredesign.storage.utils.TREE_CONST.ROOT_KEY
import il.ac.technion.cs.softwaredesign.tests.assertWithTimeout
import il.ac.technion.cs.softwaredesign.tests.isTrue
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import java.util.*

class SecureAVLTreeTest {

    data class SimpleKey(var i:Long): ISecureStorageKey<SimpleKey>{
        override fun compareTo(other: SimpleKey): Int {
           return i.compareTo(other.i)
        }

        override fun toByteArray(): ByteArray {
            return Longs.toByteArray(this.i)
        }

        override fun fromByteArray(value: ByteArray) {
            this.i=Longs.fromByteArray(value)
        }
    }

    class ByteArrayKey(private val bytes: ByteArray) {
        override fun equals(other: Any?): Boolean =
                this === other || other is ByteArrayKey && this.bytes contentEquals other.bytes
        override fun hashCode(): Int = bytes.contentHashCode()
        override fun toString(): String = bytes.contentToString()
    }
    private val storageMock= mutableMapOf<ByteArrayKey,ByteArray>()
    private var storageLayer: SecureStorage = mockk()
    private val tree=SecureAVLTree(storageLayer) {SimpleKey(0L)}
    private val blackRedTree=TreeMap<SimpleKey,SimpleKey>()

    private fun initTree() {

        storageLayer.write(ROOT_KEY.toByteArray(), Longs.toByteArray(ROOT_INIT_INDEX))
    }
    init{
        val keySlot= slot<ByteArray>()
        val valueSlot= slot<ByteArray>()
        every{
            storageLayer.write(capture(keySlot),capture(valueSlot))
        } answers {
            val value  =valueSlot.captured
            val key= ByteArrayKey(keySlot.captured)
            storageMock[key]=value
        }
        every {
            storageLayer.read(capture(keySlot))
        } answers {
            val key = ByteArrayKey(keySlot.captured)
            storageMock[key]
        }

    }

    @BeforeEach
    fun setUp(){
        initTree()

    }


    @Test
    fun isEmpty() {
        assertWithTimeout({ tree.size()==blackRedTree.size  }, isTrue)

    }

    @Test
    fun size() {

    }

    @Test
    fun height() {
    }

    @Test
    fun get() {
    }

    @Test
    fun contains() {
    }

    @Test
    fun put() {
        val value=SimpleKey(7)
        tree.put(value)
        blackRedTree[value] = value
        assertWithTimeout({ tree[value] == blackRedTree[value] }, isTrue)

        val value2=SimpleKey(8)
        tree.put(value2)
        blackRedTree[value2] = value2
        assertWithTimeout({ tree[value2] == blackRedTree[value2] }, isTrue)

        val value3=SimpleKey(6)
        tree.put(value3)
        blackRedTree[value3] = value3
        assertWithTimeout({ tree[value3] == blackRedTree[value3] }, isTrue)

        val value4=SimpleKey(2)
        tree.put(value4)
        blackRedTree[value4] = value4
        assertWithTimeout({ tree[value4] == blackRedTree[value4] }, isTrue)
    }

    @Test
    fun delete() {
    }

    @Test
    fun deleteMin() {
    }

    @Test
    fun deleteMax() {
    }

    @Test
    fun min() {
    }

    @Test
    fun max() {
    }

    @Test
    fun floor() {
    }

    @Test
    fun ceiling() {
    }

    @Test
    fun select() {
    }

    @Test
    fun rank() {
    }

    @Test
    fun keys() {
    }

    @Test
    fun keysInOrder() {
    }

    @Test
    fun keysLevelOrder() {
    }


}