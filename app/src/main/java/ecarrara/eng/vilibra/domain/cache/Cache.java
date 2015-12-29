package ecarrara.eng.vilibra.domain.cache;

/**
 * Generic cache interface definition.
 */
public interface Cache <Key, Type> {

    Type get(Key elementKey);
    void put(Key elementKey, Type elementToBeCached);
    void remove(Key elementKeyToBeRemoved);
    boolean isCached(Key elementKey);
    void clear();

}