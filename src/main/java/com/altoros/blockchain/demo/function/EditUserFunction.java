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
public class EditUserFunction implements ChaincodeFunction<User> {

    private static Log log = LogFactory.getLog(EditUserFunction.class);

    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    public EditUserFunction(EntityManager entityManager, ObjectMapper objectMapper) {
        this.entityManager = entityManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public User invoke(ChaincodeStub stub, String[] args) throws Exception {
        if (args.length != 1) {
            throw new ChaincodeFunctionException("Argument (user json) is required");
        }

        String editedUserStr = args[0];

        User editedUser = objectMapper.readValue(editedUserStr, User.class);
        long userId = editedUser.getId();

        User user = entityManager.get(stub, userId, User.class);
        if (user == null) {
            throw new ChaincodeFunctionException("User with id=" + userId + " is not found");
        }

        editedUser = entityManager.save(stub, editedUser);

        log.info("Edited user=" + editedUser.getId());

        return editedUser;
    }
}
