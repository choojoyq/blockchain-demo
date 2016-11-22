package com.altoros.blockchain.demo.function;

import com.altoros.blockchain.demo.model.User;
import com.altoros.blockchain.demo.persistence.EntityManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.java.shim.ChaincodeStub;

/**
 * @author Nikita Gorbachevski
 */
public class SaveUserFunction implements ChaincodeFunction<User> {

    private static Log log = LogFactory.getLog(SaveUserFunction.class);

    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    public SaveUserFunction(EntityManager entityManager, ObjectMapper objectMapper) {
        this.entityManager = entityManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public User invoke(ChaincodeStub stub, String[] args) throws Exception {
        if (args.length != 1) {
            throw new ChaincodeFunctionException("Argument (user json) is required");
        }

        String userStr = args[0];

        User user = objectMapper.readValue(userStr, User.class);

        user = entityManager.save(stub, user);

        log.info("Saved new user=" + user.getId());

        return user;
    }
}
