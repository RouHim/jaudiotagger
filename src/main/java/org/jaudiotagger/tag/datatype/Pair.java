package org.jaudiotagger.tag.datatype;

/**
 * A pair of values
 * <p>
 * USed by TIPL, TMCL and IPLS frames that store pairs of values
 */
public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        setKey(key);
        setValue(value);
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public String getPairValue() {
        return getKey() + "\0" + getValue();
    }
}
