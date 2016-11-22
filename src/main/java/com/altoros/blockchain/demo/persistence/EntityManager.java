package com.altoros.blockchain.demo.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperledger.java.shim.ChaincodeStub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Nikita Gorbachevski
 */
public class EntityManager {

    private static final String KEY_SEPARATOR = ":";

    private final ObjectMapper objectMapper;

    public EntityManager(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T extends Persistable> T save(ChaincodeStub stub, T entity) throws Exception {
        // generate id for a new entity
        if (entity.getId() == 0) {
            long id = nextId(stub, entity.getIdGeneratorName());
            entity.setId(id);
        }
        String key = getKey(entity.getId(), entity.getClass());
        stub.putState(key, objectMapper.writeValueAsString(entity));
        return entity;
    }

    public <T extends Persistable> void remove(ChaincodeStub stub, T entity) {
        String key = getKey(entity.getId(), entity.getClass());
        stub.delState(key);
    }

    public <T extends Persistable> T get(ChaincodeStub stub, long id, Class<T> clazz) throws Exception {
        String key = getKey(id, clazz);
        String entityStr = stub.getState(key);
        if (entityStr == null || entityStr.isEmpty()) {
            return null;
        }
        return objectMapper.readValue(entityStr, clazz);
    }

    public <T extends Persistable> List<T> getList(ChaincodeStub stub, Class<T> clazz) throws Exception {
        Map<String, String> values = stub.rangeQueryState("", "");
        List<T> ts = new ArrayList<>();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String entryKey = entry.getKey();
            int pos = entryKey.indexOf(KEY_SEPARATOR);
            // generator
            if (pos == -1) {
                continue;
            }
            if (entryKey.substring(0, entryKey.indexOf(KEY_SEPARATOR)).equals(clazz.getCanonicalName())) {
                T t = objectMapper.readValue(entry.getValue(), clazz);
                ts.add(t);
            }
        }
        return ts;
    }

    private String getKey(long id, Class<?> clazz) {
        return clazz.getCanonicalName() + KEY_SEPARATOR + id;
    }

    private long nextId(ChaincodeStub stub, String generatorName) {
        String generatorValue = stub.getState(generatorName);

        long nextId;
        if (generatorValue == null || generatorValue.isEmpty()) {
            nextId = 1L;
        } else {
            nextId = Long.valueOf(generatorValue) + 1;
        }

        stub.putState(generatorName, String.valueOf(nextId));
        return nextId;
    }
}
