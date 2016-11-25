package com.altoros.blockchain.demo;

import com.altoros.blockchain.demo.function.ChaincodeFunction;
import com.altoros.blockchain.demo.function.ChaincodeFunctionType;
import com.altoros.blockchain.demo.function.ChaincodeFunctionsFactory;
import com.altoros.blockchain.demo.model.Error;
import com.altoros.blockchain.demo.persistence.EntityManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.java.shim.ChaincodeBase;
import org.hyperledger.java.shim.ChaincodeStub;

/**
 * @author Nikita Gorbachevski
 */
public class ChaincodeDemo extends ChaincodeBase {

    private static Log log = LogFactory.getLog(ChaincodeDemo.class);

    private final ObjectMapper objectMapper;
    private final ChaincodeFunctionsFactory chaincodeFunctionsFactory;

    public ChaincodeDemo(ObjectMapper objectMapper,
                         ChaincodeFunctionsFactory chaincodeFunctionsFactory) {
        this.objectMapper = objectMapper;
        this.chaincodeFunctionsFactory = chaincodeFunctionsFactory;
    }

    @Override
    public String run(ChaincodeStub stub, String function, String[] args) {
        return process(stub, function, args, ChaincodeFunctionType.RUN);
    }

    @Override
    public String query(ChaincodeStub stub, String function, String[] args) {
        return process(stub, function, args, ChaincodeFunctionType.QUERY);
    }

    @Override
    public String getChaincodeID() {
        return "demo";
    }

    public static void main(String[] args) {
        ObjectMapper objectMapper = new CustomObjectMapper();
        EntityManager entityManager = new EntityManager(objectMapper);
        ChaincodeFunctionsFactory chaincodeFunctionsFactory =
                new ChaincodeFunctionsFactory(entityManager, objectMapper);
        new ChaincodeDemo(objectMapper, chaincodeFunctionsFactory).start(args);
    }

    private String process(ChaincodeStub chaincodeStub, String function, String[] args, ChaincodeFunctionType type) {
        log.info("Running function=" + function);
        Object res;
        try {
//            authenticationFilter.doFilter(chaincodeStub, args);
            ChaincodeFunction chaincodeFunction = chaincodeFunctionsFactory.getFunction(function, type);
            res = chaincodeFunction.invoke(chaincodeStub, args);
            log.debug("Invocation result=" + res);
        } catch (Exception e) {
            log.error("Error during invocation of function=" + function, e);
            res = new Error(e.getMessage());
        }
        try {
            return objectMapper.writeValueAsString(res);
        } catch (JsonProcessingException e) {
            log.error("Error during json serialization", e);
            return "{\"message\":\"Internal server error\"}";
        }
    }
}
