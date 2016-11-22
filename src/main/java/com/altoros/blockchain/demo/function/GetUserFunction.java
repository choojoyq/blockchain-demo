package com.altoros.blockchain.demo.function;

import com.altoros.blockchain.demo.model.User;
import com.altoros.blockchain.demo.persistence.EntityManager;
import org.hyperledger.java.shim.ChaincodeStub;

/**
 * @author Nikita Gorbachevski
 */
public class GetUserFunction implements ChaincodeFunction<User> {

    private final EntityManager entityManager;

    public GetUserFunction(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User invoke(ChaincodeStub stub, String[] args) throws Exception {
        if (args.length != 1) {
            throw new ChaincodeFunctionException("Argument (user id) is required");
        }

        long userId = Long.valueOf(args[0]);

        return entityManager.get(stub, userId, User.class);
    }
}
