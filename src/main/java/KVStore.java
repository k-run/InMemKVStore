import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class KVStore {
    private final ConcurrentHashMap<String, Map<String, Object>> store;
    private final Map<String, Class<?>> attributeType;
    private final ReentrantReadWriteLock lock;

    public KVStore() {
        this.store = new ConcurrentHashMap<>();
        this.attributeType = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public Object get(String key) {
        return store.get(key);
    }

    public void put(String key, List<Pair<String, String>> attrs) {
        Map<String, Object> attrsMap = new HashMap<>();
        for (Pair<String, String> pair: attrs) {
            String attrKey = pair.getKey();
            String attrVal = pair.getValue();
            Object value = parseValue(attrVal);
            validateAttrType(attrKey, value);
            attrsMap.put(attrKey, value);
        }

        store.put(key, attrsMap);
    }

    public List<String> search(String key, String val) {
        List<String> res = new ArrayList<>();
        for (Map.Entry<String, Map<String, Object>> entry: store.entrySet()) {
            Object value = entry.getValue().get(key);
            if(value != null && valueEquals(value, val)) {
                res.add(entry.getKey());
            }
        }

        return res;
    }

    public void delete(String key) {
        store.remove(key);
    }

    public List<String> keys() {return new ArrayList<>(store.keySet());}

    /*
        Gets the expected data type for a given value
        and if the expected data type is not found in the map, puts it against the key
        else if the expected data type doesn't match with the given value's, throws an exception

     */
    private void validateAttrType(String key, Object value) {
        lock.writeLock().lock();
        try {
            Class<?> expectedType = attributeType.get(key);
            if(expectedType == null) {
                attributeType.put(key, value.getClass());
            }
            else if(!expectedType.equals(value.getClass())) {
                throw new IllegalArgumentException("Data type error");
            }
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    /*
        Checks if a given string is an int, if yes asks Integer to parse and return the value
        If it's a double, asks Double to parse and return value
        If it's a boolean, asks Boolean to parse and return value
        else return value as it's a string
     */
    private Object parseValue(String value) {
        if(value.matches("-?\\d+")) {
            return Integer.parseInt(value);
        }
        else if(value.matches("-?\\d+(\\.\\d+)?")) {
            return Double.parseDouble(value);
        }
        else if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(value);
        }
        else return value;
    }

    /*
        Convert the given stringvalue to an object and compares the converted object to given object
     */

    private boolean valueEquals(Object actualValue, String stringValue) {
        Object parsedValue = parseValue(stringValue);
        return actualValue.equals(parsedValue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Map<String, Object>> entry : store.entrySet()) {
            sb.append(entry.getKey()).append(": ");
            for (Map.Entry<String, Object> attrEntry : entry.getValue().entrySet()) {
                sb.append(attrEntry.getKey()).append(": ").append(attrEntry.getValue()).append(", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append("\n");
        }
        return sb.toString();
    }
}
