package il.ac.technion.cs.softwaredesign

import java.util.LinkedHashMap

class LRUCache<K, V>(private val capacity: Int) :
        LinkedHashMap<K, V>(capacity + 1, 0.75f, true) {

    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
        return size > this.capacity
    }
}