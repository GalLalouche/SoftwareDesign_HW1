package il.ac.technion.cs.softwaredesign.storage.datastructures

import com.google.common.primitives.Longs
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import il.ac.technion.cs.softwaredesign.storage.api.ISecureStorageKey
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.utils.TREE_CONST.ROOT_INIT_INDEX
import il.ac.technion.cs.softwaredesign.storage.utils.TREE_CONST.ROOT_KEY
import il.ac.technion.cs.softwaredesign.tests.assertWithTimeout
import il.ac.technion.cs.softwaredesign.tests.isFalse
import il.ac.technion.cs.softwaredesign.tests.isTrue
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.random.Random


class SecureAVLTreeTest {

    data class SimpleKey(var i:Long): ISecureStorageKey<SimpleKey> {
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
    private val blackRedTree= TreeMap<SimpleKey,SimpleKey>()

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
        assertThat(tree.size(), equalTo(blackRedTree.size.toLong()))
    }

    @Test
    fun size() {

    }

    @Test
    fun height() {
    }

    @Test
    fun get() {
        val values = (1..20).map { SimpleKey(it.toLong()) }
        values.forEach({tree.put(it); blackRedTree[it]=it})
        values.forEach({assertWithTimeout({tree[it] == blackRedTree[it]}, isTrue)})
    }

    @Test
    fun contains() {
        val values = (1..20).map { SimpleKey(it.toLong()) }
        values.forEach({tree.put(it); blackRedTree[it]=it})
        values.forEach({assertWithTimeout({tree.contains(it)}, isTrue)})
        val notInsertedValues = (25..40).map { SimpleKey(it.toLong()) }
        notInsertedValues.forEach({assertWithTimeout({tree.contains(it)}, isFalse)})
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
        tree.delete(SimpleKey(50))

        val values = (1..3).map { SimpleKey(it.toLong()) }
        values.forEach {tree.put(it)}
        assertWithTimeout({ tree.contains(values[1]) }, isTrue)
        tree.delete(values[1])
        assertWithTimeout({ tree.contains(values[1]) }, isFalse)
        val moreValues = (1..20).map { SimpleKey(it.toLong()) }
        moreValues.forEach {tree.put(it)}

        assertWithTimeout({ tree.contains(moreValues[8]) }, isTrue)
        tree.delete(moreValues[8])
        assertWithTimeout({ tree.contains(moreValues[8]) }, isFalse)

        assertWithTimeout({ tree.contains(moreValues[11]) }, isTrue)
        tree.delete(moreValues[11])
        assertWithTimeout({ tree.contains(moreValues[11]) }, isFalse)

        assertWithTimeout({ tree.contains(moreValues[1]) }, isTrue)
        tree.delete(moreValues[1])
        assertWithTimeout({ tree.contains(moreValues[1]) }, isFalse)

        assertWithTimeout({ tree.contains(SimpleKey(50)) }, isFalse)
        tree.delete(SimpleKey(50))
        assertWithTimeout({ tree.contains(SimpleKey(50)) }, isFalse)
    }

    @Test
    fun deleteMin() {
//        val random : Random = Random()
//        (1..20).forEach({val v = SimpleKey(random.nextLong()); tree.put(v); blackRedTree[v]=v})
//        val min = blackRedTree.entries.first()
//        tree.deleteMin()

    }

    @Test
    fun deleteMax() {
    }

    @Test
    fun min() {
//        var minValue:Long= Long.MAX_VALUE
//        for(i in 1..2000){
//            val v = SimpleKey(Random.nextLong(from=ROOT_INIT_INDEX+1, until=Long.MAX_VALUE))
//            minValue=min(minValue,v.i)
//            tree.put(v)
//        }
//         val treeMin = tree.min().i
//         assertThat(treeMin, equalTo(minValue))
    }

    @Test
    fun max() {
        for(i in 1..200){
            val v = SimpleKey(Random.nextLong(from=ROOT_INIT_INDEX+1, until=Long.MAX_VALUE))
            blackRedTree[v] = v
            tree.put(v)
        }
        assertThat(tree.max(), equalTo(blackRedTree.lastEntry().key))
    }

    @Test
    fun floor() {
    }

    @Test
    fun ceiling() {
    }

    @Test
    fun select() {
        for(i in 1..200){
            val v = SimpleKey(Random.nextLong(from=ROOT_INIT_INDEX+1, until=Long.MAX_VALUE))
            blackRedTree[v] = v
            tree.put(v)
        }
        var k = 0L
        for (res in blackRedTree) {
            val current = tree.select(k)
            assertThat(current, equalTo(res.key))
            k++
        }
    }

    @Test
    fun rank() {
    }

    @Test
    fun keys() {
    }

    @Test
    fun keysInOrder() {
        for(i in 1..200){
            val v = SimpleKey(Random.nextLong(from=ROOT_INIT_INDEX+1, until=Long.MAX_VALUE))
            blackRedTree[v] = v
            tree.put(v)
        }
        val values = tree.keysInOrder().iterator()
        for (res in blackRedTree) {
            val current = values.next()
            assertThat(current, equalTo(res.key))
        }
    }

    @Test
    fun keysLevelOrder() {
    }


}