package com.altoros.blockchain.demo.function;

import com.altoros.blockchain.demo.model.User;
import com.altoros.blockchain.demo.persistence.EntityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.java.shim.ChaincodeStub;

/**
 * @author Nikita Gorbachevski
 */
public class RemoveUserFunction implements ChaincodeFunction<Void> {

    private static Log log = LogFactory.getLog(SaveUserFunction.class);

    private final EntityManager entityManager;

    public RemoveUserFunction(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Void invoke(ChaincodeStub stub, String[] args) throws Exception {
        if (args.length != 1) {
            throw new ChaincodeFunctionException("Argument (user id) is required");
        }

        long userId = Long.valueOf(args[0]);

        User user = entityManager.get(stub, userId, User.class);
        if (user == null) {
            throw new ChaincodeFunctionException("User with id=" + userId + " is not found");
        }

        entityManager.remove(stub, user);

        log.info("Removed user=" + user.getId());

        return null;
    }
}
