package server.datastore;

import java.util.HashMap;

public class KeyValueStore {
    /**
     * Key Value Store that stores the key and value that are received from the client.
     */
    HashMap<String, String> keyValueMap = new HashMap<String, String>();

    public KeyValueStore() {
    }

    /**
     * Adds the key and value pair to the storage.
     *
     * @param key   key of the store.
     * @param value value of the corresponding key.
     */
    public void put(String key, String value) {
        keyValueMap.put(key, value);
    }

    /**
     * Returns the value of the corresponding key
     *
     * @param key key to which value needs to be found
     * @return value of the key given.
     */
    public String get(String key) {
        return keyValueMap.get(key);
    }


    /**
     * Deletes the key pair from the store.
     *
     * @param key - key pair to be removed.
     * @return previous value of the key.
     */
    public String delete(String key) {
        return keyValueMap.remove(key);
    }

}