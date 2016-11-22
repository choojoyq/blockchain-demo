package com.altoros.blockchain.demo.function;

import com.altoros.blockchain.demo.persistence.EntityManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nikita Gorbachevski
 */
public class ChaincodeFunctionsFactory {

    private final Map<ChaincodeFunctionType, Map<String, ChaincodeFunction>> typeFunctions;

    public ChaincodeFunctionsFactory(EntityManager entityManager, ObjectMapper objectMapper) {
        Map<String, ChaincodeFunction> functionsQuery = new HashMap<>();
        functionsQuery.put("getUser", new GetUserFunction(entityManager));
        functionsQuery.put("getUsers", new GetUsersFunction(entityManager));

        Map<String, ChaincodeFunction> functionsRun = new HashMap<>();
        functionsRun.put("init", (stub, args) -> null);
        functionsRun.put("saveUser", new SaveUserFunction(entityManager, objectMapper));
        functionsRun.put("editUser", new EditUserFunction(entityManager, objectMapper));
        functionsRun.put("removeUser", new RemoveUserFunction(entityManager));

        typeFunctions = new HashMap<>();
        typeFunctions.put(ChaincodeFunctionType.RUN, functionsRun);
        typeFunctions.put(ChaincodeFunctionType.QUERY, functionsQuery);
    }

    public ChaincodeFunction getFunction(String functionName, ChaincodeFunctionType type)
            throws ChaincodeFunctionException {
        Map<String, ChaincodeFunction> functions = typeFunctions.get(type);
        if (functions == null) {
            throw new ChaincodeFunctionException("Unknown function type=" + type);
        }
        ChaincodeFunction function = functions.get(functionName);
        if (function == null) {
            throw new ChaincodeFunctionException("Unknown function=" + functionName);
        }
        return function;
    }
}
