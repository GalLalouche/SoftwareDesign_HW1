package il.ac.technion.cs.softwaredesign.storage.datastructures

import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.ISecureStorageKey
import il.ac.technion.cs.softwaredesign.storage.IStorageConverter
import il.ac.technion.cs.softwaredesign.storage.LEFT_KEY
import java.util.*
import javax.inject.Inject

/**
 * The `SecureAVLTree` class represents an ordered symbol table of
 * generic key-value pairs. It supports the usual *put*, *get*,
 * *contains*, *delete*, *size*, and *is-empty*
 * methods. It also provides ordered methods for finding the *minimum*,
 * *maximum*, *floor*, and *ceiling*. It also provides a
 * *keys* method for iterating over all of the keys. A symbol table
 * implements the *associative array* abstraction: when associating a
 * value with a key that is already in the symbol table, the convention is to
 * replace the old value with the new value. Unlike [java.util.Map], this
 * class uses the convention that values cannot be `null`
 * —setting the value associated with a key to `null` is
 * equivalent to deleting the key from the symbol table.
 *
 *
 * This symbol table implementation uses internally an
 * [ AVL tree ](https://en.wikipedia.org/wiki/AVL_tree) (Georgy
 * Adelson-Velsky and Evgenii Landis' tree) which is a self-balancing BST.
 * In an AVL tree, the heights of the two child subtrees of any
 * node differ by at most one; if at any time they differ by more than one,
 * rebalancing is done to restore this property.
 *
 *
 * This implementation requires that the key type implements the
 * `ISecureStorageKey` interface and calls the `compareTo()` and
 * method to compare two keys and 'toByteArray' 'fromByteArray' to permit a way for serialization of the key without conflicts(this is client responsibility). It does not call either `equals()` or
 * `hashCode()`. The *put*, *get*, *contains*,
 * *delete*, *minimum*, *maximum*, *ceiling*, and
 * *floor* operations each take logarithmic time in the worst case. The
 * *size*, and *is-empty* operations take constant time.
 * Construction also takes constant time.
 *
 *
 * @author Aviad Shiber, Ron Yithak, Marcelo Silva
 */

/**
 * Initializes an empty symbol table.
 */
class SecureAVLTree<Key : ISecureStorageKey<Key>, Value :IStorageConverter<Value>>
@Inject constructor(private val secureStorage : SecureStorage) {

    /**
     * The root node.
     */
    private var root: Node? = null



    /**
     * Checks if rank is consistent.
     *
     * @return `true` if rank is consistent
     */
    private val isRankConsistent: Boolean
        get() {
            for (i in 0 until size())
                if (i != rank(select(i))) return false
            for (key in keys())
                if (key.compareTo(select(rank(key))) != 0) return false
            return true
        }

    /**
     * Checks if the symbol table is empty.
     *
     * @return `true` if the symbol table is empty.
     */
    fun isEmpty(): Boolean = root == null

    /**
     * Checks if AVL property is consistent.
     *
     * @return `true` if AVL property is consistent.
     */
    private fun isAVL(): Boolean = isAVL(root)

    /**
     * Checks if the symmetric order is consistent.
     *
     * @return `true` if the symmetric order is consistent
     */
    private fun isBST(): Boolean = isBST(root, null, null)

    /**
     * Checks if size is consistent.
     *
     * @return `true` if size is consistent
     */
    private fun isSizeConsistent(): Boolean = isSizeConsistent(root)

    /**
     * Returns the number key-value pairs in the symbol table.
     *
     * @return the number key-value pairs in the symbol table
     */
    fun size(): Int {
        return size(root)
    }

    /**
     * Returns the number of nodes in the subtree.
     *
     * @param x the subtree
     *
     * @return the number of nodes in the subtree
     */
    private fun size(x: Node?): Int {
        return x?.size ?: 0
    }

    /**
     * Returns the height of the internal AVL tree. It is assumed that the
     * height of an empty tree is -1 and the height of a tree with just one node
     * is 0.
     *
     * @return the height of the internal AVL tree (-1 is returned if tree is empty)
     */
    fun height(): Int {
        return height(root)
    }

    /**
     * Returns the height of the subtree.
     *
     * @param x the subtree
     *
     * @return the height of the subtree -1 is returned if x subtree is empty
     */
    private fun height(x: Node?): Int {
        return x?.height ?: -1
    }

    /**
     * Returns the value associated with the given key.
     *
     * @param key the key
     * @return the value associated with the given key if the key is in the
     * symbol table and `null` if the key is not in the
     * symbol table
     */
    operator fun get(key: Key): Value? {
        val x = get(root, key) ?: return null
        return x.value
    }


    /**
     * Returns value associated with the given key in the subtree or
     * `null` if no such key.
     *
     * @param x the subtree
     * @param key the key
     * @return value associated with the given key in the subtree or
     * `null` if no such key
     */
    private operator fun get(x: Node?, key: Key): Node? {
        if (x == null) return null
        val cmp = key.compareTo(x.key)
        return when {
            cmp < 0 -> get(x.left, key)
            cmp > 0 -> get(x.right, key)
            else -> x
        }
    }

    /**
     * Checks if the symbol table contains the given key.
     *
     * @param key the key
     * @return `true` if the symbol table contains `key`
     * and `false` otherwise
     */
    operator fun contains(key: Key): Boolean {
        return get(key) != null
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting
     * the old value with the new value if the symbol table already contains the
     * specified key. Deletes the specified key (and its associated value) from
     * this symbol table if the specified value is `null`.
     *
     * @param key the key
     * @param `value` the value
     */
    fun put(key: Key, value: Value?) {
        if (value == null) {
            delete(key)
            return
        }
        root = put(root, key, value)
        assert(check())
    }

    /**
     * Inserts the key-value pair in the subtree. It overrides the old value
     * with the new value if the symbol table already contains the specified key
     * and deletes the specified key (and its associated value) from this symbol
     * table if the specified value is `null`.
     *
     * @param x the subtree
     * @param key the key
     * @param `value` the value
     * @return the subtree
     */
    private fun put(x: Node?, key: Key, value: Value): Node {
        if (x == null) return Node(key, value, 0, 1)
        val cmp = key.compareTo(x.key)
        when {
            cmp < 0 -> x.left = put(x.left, key, value)
            cmp > 0 -> x.right = put(x.right, key, value)
            else -> {
                x.value = value
                return x
            }
        }
        x.size = 1 + size(x.left) + size(x.right)
        x.height = 1 + Math.max(height(x.left), height(x.right))
        return balance(x)
    }

    /**
     * Restores the AVL tree property of the subtree.
     *
     * @param x the subtree
     * @return the subtree with restored AVL property
     */
    private fun balance(x: Node): Node {
        var traversalX = x
        if (balanceFactor(traversalX) < -1) {
            if (balanceFactor(traversalX.right!!) > 0) {
                traversalX.right = rotateRight(traversalX.right!!)
            }
            traversalX = rotateLeft(traversalX)
        } else if (balanceFactor(traversalX) > 1) {
            if (balanceFactor(traversalX.left!!) < 0) {
                traversalX.left = rotateLeft(traversalX.left!!)
            }
            traversalX = rotateRight(traversalX)
        }
        return traversalX
    }

    /**
     * Returns the balance factor of the subtree. The balance factor is defined
     * as the difference in height of the left subtree and right subtree, in
     * this order. Therefore, a subtree with a balance factor of -1, 0 or 1 has
     * the AVL property since the heights of the two child subtrees differ by at
     * most one.
     *
     * @param x the subtree
     * @return the balance factor of the subtree
     */
    private fun balanceFactor(x: Node): Int {
        return height(x.left) - height(x.right)
    }

    /**
     * Rotates the given subtree to the right.
     *
     * @param x the subtree
     * @return the right rotated subtree
     */
    private fun rotateRight(x: Node): Node {
        val y = x.left
        x.left = y!!.right
        y.right = x
        y.size = x.size
        x.size = 1 + size(x.left) + size(x.right)
        x.height = 1 + Math.max(height(x.left), height(x.right))
        y.height = 1 + Math.max(height(y.left), height(y.right))
        return y
    }

    /**
     * Rotates the given subtree to the left.
     *
     * @param x the subtree
     * @return the left rotated subtree
     */
    private fun rotateLeft(x: Node): Node {
        val y = x.right
        x.right = y!!.left
        y.left = x
        y.size = x.size
        x.size = 1 + size(x.left) + size(x.right)
        x.height = 1 + Math.max(height(x.left), height(x.right))
        y.height = 1 + Math.max(height(y.left), height(y.right))
        return y
    }

    /**
     * Removes the specified key and its associated value from the symbol table
     * (if the key is in the symbol table).
     * @param key the key
     */
    fun delete(key: Key) {
        if (!contains(key)) return
        root = delete(root!!, key)
        assert(check())
    }

    /**
     * Removes the specified key and its associated value from the given
     * subtree.
     *
     * @param x the subtree
     * @param key the key
     * @return the updated subtree
     */
    private fun delete(x: Node, key: Key): Node? {
        var node = x
        val cmp = key.compareTo(node.key)
        when {
            cmp < 0 -> node.left = delete(node.left!!, key)
            cmp > 0 -> node.right = delete(node.right!!, key)
            else -> when {
                node.left == null -> return node.right
                node.right == null -> return node.left
                else -> {
                    val y = node
                    node = min(y.right!!)
                    node.right = deleteMin(y.right!!)
                    node.left = y.left
                }
            }
        }
        node.size = 1 + size(node.left) + size(node.right)
        node.height = 1 + Math.max(height(node.left), height(node.right))
        return balance(node)
    }

    /**
     * Removes the smallest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun deleteMin() {
        if (isEmpty()) throw NoSuchElementException("called deleteMin() with empty symbol table")
        root = deleteMin(root!!)
        assert(check())
    }

    /**
     * Removes the smallest key and associated value from the given subtree.
     *
     * @param x the subtree
     * @return the updated subtree
     */
    private fun deleteMin(x: Node): Node? {
        if (x.left == null) return x.right
        x.left = deleteMin(x.left!!)
        x.size = 1 + size(x.left) + size(x.right)
        x.height = 1 + Math.max(height(x.left), height(x.right))
        return balance(x)
    }

    /**
     * Removes the largest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun deleteMax() {
        if (isEmpty()) throw NoSuchElementException("called deleteMax() with empty symbol table")
        root = deleteMax(root!!)
        assert(check())
    }

    /**
     * Removes the largest key and associated value from the given subtree.
     *
     * @param x the subtree
     * @return the updated subtree
     */
    private fun deleteMax(x: Node): Node? {
        if (x.right == null) return x.left
        x.right = deleteMax(x.right!!)
        x.size = 1 + size(x.left) + size(x.right)
        x.height = 1 + Math.max(height(x.left), height(x.right))
        return balance(x)
    }

    /**
     * Returns the smallest key in the symbol table.
     *
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun min(): Key {
        if (isEmpty()) throw NoSuchElementException("called min() with empty symbol table")
        return min(root!!).key
    }

    /**
     * Returns the node with the smallest key in the subtree.
     *
     * @param x the subtree
     * @return the node with the smallest key in the subtree
     */
    private fun min(x: Node): Node {
        return if (x.left == null) x else min(x.left!!)
    }

    /**
     * Returns the largest key in the symbol table.
     *
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun max(): Key {
        if (isEmpty()) throw NoSuchElementException("called max() with empty symbol table")
        return max(root!!).key
    }

    /**
     * Returns the node with the largest key in the subtree.
     *
     * @param x the subtree
     * @return the node with the largest key in the subtree
     */
    private fun max(x: Node): Node {
        return if (x.right == null) x else max(x.right!!)
    }

    /**
     * Returns the largest key in the symbol table less than or equal to
     * `key`.
     *
     * @param key the key
     * @return the largest key in the symbol table less than or equal to
     * `key`
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun floor(key: Key): Key? {
        if (isEmpty()) throw NoSuchElementException("called floor() with empty symbol table")
        val x = floor(root, key)
        return x?.key
    }

    /**
     * Returns the node in the subtree with the largest key less than or equal
     * to the given key.
     *
     * @param x the subtree
     * @param key the key
     * @return the node in the subtree with the largest key less than or equal
     * to the given key
     */
    private fun floor(x: Node?, key: Key): Node? {
        if (x == null) return null
        val cmp = key.compareTo(x.key)
        if (cmp == 0) return x
        if (cmp < 0) return floor(x.left, key)
        val y = floor(x.right, key)
        return y ?: x
    }

    /**
     * Returns the smallest key in the symbol table greater than or equal to
     * `key`.
     *
     * @param key the key
     * @return the smallest key in the symbol table greater than or equal to
     * `key`
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun ceiling(key: Key): Key? {
        if (isEmpty()) throw NoSuchElementException("called ceiling() with empty symbol table")
        val x = ceiling(root, key)
        return x?.key
    }

    /**
     * Returns the node in the subtree with the smallest key greater than or
     * equal to the given key.
     *
     * @param x the subtree
     * @param key the key
     * @return the node in the subtree with the smallest key greater than or
     * equal to the given key
     */
    private fun ceiling(x: Node?, key: Key): Node? {
        if (x == null) return null
        val cmp = key.compareTo(x.key)
        if (cmp == 0) return x
        if (cmp > 0) return ceiling(x.right, key)
        val y = ceiling(x.left, key)
        return y ?: x
    }

    /**
     * Returns the kth smallest key in the symbol table.
     *
     * @param k the order statistic
     * @return the kth smallest key in the symbol table
     * @throws IllegalArgumentException unless `k` is between 0 and
     * `size() -1 `
     */
    fun select(k: Int): Key {
        if (k < 0 || k >= size()) throw IllegalArgumentException("key is out of range. should be between [0,${size()-1}]}")
        val x = select(root, k)
        return x!!.key
    }

    /**
     * Returns the node with key the kth smallest key in the subtree.
     *
     * @param x the subtree
     * @param k the kth smallest key in the subtree
     * @return the node with key the kth smallest key in the subtree
     */
    private fun select(x: Node?, k: Int): Node? {
        if (x == null) return null
        val t = size(x.left)
        return when {
            t > k -> select(x.left, k)
            t < k -> select(x.right, k - t - 1)
            else -> x
        }
    }

    /**
     * Returns the number of keys in the symbol table strictly less than
     * `key`.
     *
     * @param key the key
     * @return the number of keys in the symbol table strictly less than
     * `key`
     */
    fun rank(key: Key): Int {
        return rank(key, root)
    }

    /**
     * Returns the number of keys in the subtree less than key.
     *
     * @param key the key
     * @param x the subtree
     * @return the number of keys in the subtree less than key
     */
    private fun rank(key: Key, x: Node?): Int {
        if (x == null) return 0
        val cmp = key.compareTo(x.key)
        return when {
            cmp < 0 -> rank(key, x.left)
            cmp > 0 -> 1 + size(x.left) + rank(key, x.right)
            else -> size(x.left)
        }
    }

    /**
     * Returns all keys in the symbol table.
     *
     * @return all keys in the symbol table
     */
    fun keys(): Iterable<Key> {
        return keysInOrder()
    }

    /**
     * Returns all keys in the symbol table following an in-order traversal.
     *
     * @return all keys in the symbol table following an in-order traversal
     */
    fun keysInOrder(): Iterable<Key> {
        val queue = mutableListOf<Key>()
        keysInOrder(root, queue)
        return queue
    }

    /**
     * Adds the keys in the subtree to queue following an in-order traversal.
     *
     * @param x the subtree
     * @param queue the queue
     */
    private fun keysInOrder(x: Node?, queue: MutableList<Key>) {
        if (x == null) return
        keysInOrder(x.left, queue)
        queue.add(x.key)
        keysInOrder(x.right, queue)
    }

    /**
     * Returns all keys in the symbol table following a level-order traversal.
     *
     * @return all keys in the symbol table following a level-order traversal.
     */
    fun keysLevelOrder(): Iterable<Key> {
        val queue = mutableListOf<Key>()
        if (!isEmpty()) {
            val queue2=mutableListOf<Node>()
            queue2.add(root!!)
            while (queue2.isNotEmpty()) {
                val x = queue2.removeAt(0)
                queue.add(x.key)
                if (x.left != null) {
                    queue2.add(x.left!!)
                }
                if (x.right != null) {
                    queue2.add(x.right!!)
                }
            }
        }
        return queue
    }

    /**
     * Returns all keys in the symbol table in the given range.
     *
     * @param lo the lowest key
     * @param hi the highest key
     * @return all keys in the symbol table between `lo` (inclusive)
     * and `hi` (exclusive)
     */
    fun keys(lo: Key, hi: Key): Iterable<Key> {
        val queue = mutableListOf<Key>()
        keys(root, queue, lo, hi)
        return queue
    }

    /**
     * Adds the keys between `lo` and `hi` in the subtree
     * to the `queue`.
     *
     * @param x the subtree
     * @param queue the queue
     * @param lo the lowest key
     * @param hi the highest key
     */
    private fun keys(x: Node?, queue: MutableList<Key>, lo: Key, hi: Key) {
        if (x == null) return
        val cmplo = lo.compareTo(x.key)
        val cmphi = hi.compareTo(x.key)
        if (cmplo < 0) keys(x.left, queue, lo, hi)
        if (cmplo <= 0 && cmphi >= 0) queue.add(x.key)
        if (cmphi > 0) keys(x.right, queue, lo, hi)
    }

    /**
     * Returns the number of keys in the symbol table in the given range.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return the number of keys in the symbol table between `lo`
     * (inclusive) and `hi` (exclusive)
     */
    fun size(lo: Key, hi: Key): Int {
        if (lo > hi) return 0
        return if (contains(hi))
            rank(hi) - rank(lo) + 1
        else
            rank(hi) - rank(lo)
    }

    /**
     * Checks if the AVL tree invariants are fine.
     *
     * @return `true` if the AVL tree invariants are fine
     */
    private fun check(): Boolean {
        if (!isBST()) println("Symmetric order not consistent")
        if (!isAVL()) println("AVL property not consistent")
        if (!isSizeConsistent()) println("Subtree counts not consistent")
        if (!isRankConsistent) println("Ranks not consistent")
        return isBST() && isAVL() && isSizeConsistent() && isRankConsistent
    }

    /**
     * Checks if AVL property is consistent in the subtree.
     *
     * @param x the subtree
     * @return `true` if AVL property is consistent in the subtree
     */
    private fun isAVL(x: Node?): Boolean {
        if (x == null) return true
        val bf = balanceFactor(x)
        return if (bf > 1 || bf < -1) false else isAVL(x.left) && isAVL(x.right)
    }

    /**
     * Checks if the tree rooted at x is a BST with all keys strictly between
     * min and max (if min or max is null, treat as empty constraint)
     *
     * @param x the subtree
     * @param min the minimum key in subtree
     * @param max the maximum key in subtree
     * @return `true` if if the symmetric order is consistent
     */
    private fun isBST(x: Node?, min: Key?, max: Key?): Boolean {
        if (x == null) return true
        if (min != null && x.key <= min) return false
        return if (max != null && x.key >= max) false else isBST(x.left, min, x.key) && isBST(x.right, x.key, max)
    }

    /**
     * Checks if the size of the subtree is consistent.
     *
     * @return `true` if the size of the subtree is consistent
     */
    private fun isSizeConsistent(x: Node?): Boolean {
        if (x == null) return true
        return if (x.size != size(x.left) + size(x.right) + 1) false else isSizeConsistent(x.left) && isSizeConsistent(x.right)
    }

    /**
     * This class represents an inner node of the AVL tree.
     */
    private inner class Node(var key: Key   // the key
                             , var value: Value?       // the associated value
                             , var height: Int      // height of the subtree
                             , var size: Int        // number of nodes in subtree
    )  :IStorageConverter<Node>{
        override fun toByteArray(): ByteArray {
            TODO("convert this node to ByteArray") //To change body of created functions use File | Settings | File Templates.
        }

        override fun fromByteArray(value: ByteArray?): Node? {
            TODO("convert byte array to this node") //To change body of created functions use File | Settings | File Templates.
            return this
        }

        fun getLeftNode(x: Node):Node?{
            /*val keyToLook=String(x.key.toByteArray())+ LEFT_KEY
           return secureStorage.read(keyToLook.toByteArray()*/
            TODO("get left node from Storage")
        }
        fun getRightNode(x:Node):Node?{
            TODO("get right node from Storage")
        }
        fun getNodeValue(x:Node):Value?{
            return fromByteArray(secureStorage.read())?.value
        }



        var left: Node? = null       // left subtree
        var right: Node? = null      // right subtree
    }
}
