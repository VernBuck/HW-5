import java.util.List;
import java.util.Set;
import java.util.NoSuchElementException;
import java.util.HashSet;
import java.util.ArrayList;
/**
 * Your implementation of HashMap.
 * 
 * @author Vernon Buck
 * @version 1.0
 */
public class HashMap<K, V> implements HashMapInterface<K, V> {

    // Do not make any new instance variables.
    private MapEntry<K, V>[] backingArray;
    private int size;

    /**
     * Create a hash map with no entries. The backing array has an initial
     * capacity of {@code INITIAL_CAPACITY}.
     *
     * Use constructor chaining.
     */
    public HashMap() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Create a hash map with no entries. The backing array has an initial
     * capacity of {@code initialCapacity}.
     *
     * You may assume {@code initialCapacity} will always be positive.
     *
     * @param initialCapacity initial capacity of the backing array
     */
    public HashMap(int initialCapacity) {
        size = 0;
        backingArray = new MapEntry[initialCapacity];
    }


    @Override
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("key or value is null");
        }
        int j = Math.abs(key.hashCode() % backingArray.length);
        int i = 0;
        if (((double) size + 1) / backingArray.length > MAX_LOAD_FACTOR) {
            resizeBackingArray((backingArray.length * 2) + 3);
        }
        if (backingArray[j] == null) {
            backingArray[j] = new MapEntry<>(key, value);
            size++;
        } else if (backingArray[j].getKey() == key) {
            backingArray[j].setValue(value);
        } else {
            while (i < backingArray.length) {
                MapEntry<K, V> position =
                        backingArray[(j + i) % backingArray.length];
                if (position == null) {
                    backingArray[(j + i) % backingArray.length] =
                            new MapEntry(key, value);
                    size++;
                    return null;
                } else if (position.isRemoved()) {
                    backingArray[(j + i) % backingArray.length].setKey(key);
                    backingArray[(j + i) % backingArray.length].setValue(value);
                    backingArray[(j + i) % backingArray.length]
                            .setRemoved(false);
                    size++;
                    return null;
                } else if (position.getKey().equals(key)) {
                    V temp = backingArray[(j + i) % backingArray.length]
                            .getValue();
                    backingArray[(j + i) % backingArray.length].setValue(value);
                    size++;
                    return temp;
                }
                i++;
            }
        }
        return null;
    }


    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("invalid key");
        }
        int j = Math.abs(key.hashCode() % backingArray.length);
        for (int i = 0; i < backingArray.length; i++) {
            MapEntry<K, V> mapentry =
                    backingArray[(j + i) % backingArray.length];
            if (mapentry != null) {
                if (mapentry.getKey() == key && !(mapentry.isRemoved())) {
                    V temp = mapentry.getValue();
                    mapentry.setRemoved(true);
                    size--;
                    return temp;
                }
            }
        }
        throw new NoSuchElementException("element not here");
    }


    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("invalid key");
        }
        int j = Math.abs(key.hashCode() % backingArray.length);
        for (int i = 0; i < backingArray.length; i++) {
            MapEntry<K, V> mapentry =
                    backingArray[(j + i) % backingArray.length];
            if (mapentry != null) {
                if (mapentry.getKey() == key && !(mapentry.isRemoved())) {
                    return mapentry.getValue();
                }
            }
        }
        throw new NoSuchElementException("element not there");
    }


    @Override
    public int count(V value) {
        if (value == null) {
            throw new IllegalArgumentException("null value passed");
        }
        int c = 0;
        for (int i = 0; i < backingArray.length; i++) {
            if (backingArray[i] != null
                    && backingArray[i].getValue().equals(value)
                    && !(backingArray[i].isRemoved())) {
                c = c + 1;
            }
        }
        return c;
    }


    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("invalid key");
        }
        try {
            get(key);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }


    @Override
    public void clear() {
        size = 0;
        backingArray = new MapEntry[INITIAL_CAPACITY];
    }


    @Override
    public int size() {
        return size;
    }


    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (int i = 0; i < backingArray.length; i++) {
            if (backingArray[i] != null) {
                set.add(backingArray[i].getKey());
            }
        }
        return set;
    }


    @Override
    public List<V> values() {
        List<V> list = new ArrayList<>();
        for (MapEntry<K, V> x : backingArray) {
            if (x != null) {
                list.add(x.getValue());
            }
        }
        return list;
    }


    @Override
    public void resizeBackingArray(int length) {
        if (length < size || length < 0) {
            throw new IllegalArgumentException("invalid length");
        }
        MapEntry<K, V>[] temp = new MapEntry[length];
        for (int i = 0; i < backingArray.length; i++) {
            if (backingArray[i] != null && !(backingArray[i].isRemoved())) {
                K key = backingArray[i].getKey();
                int j = Math.abs(key.hashCode() % length);
                temp[j] = backingArray[i];
            }
        }
        backingArray = temp;
    }

    // DO NOT MODIFY OR USE CODE BEYOND THIS POINT.

    @Override
    public MapEntry<K, V>[] getArray() {
        return backingArray;
    }

}
