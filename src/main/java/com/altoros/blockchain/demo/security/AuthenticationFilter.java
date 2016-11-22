package com.altoros.blockchain.demo.security;

import com.altoros.blockchain.demo.function.ChaincodeFunctionException;
import com.altoros.blockchain.demo.model.User;
import com.altoros.blockchain.demo.persistence.EntityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.java.shim.ChaincodeStub;

/**
 * @author Nikita Gorbachevski
 */
public class AuthenticationFilter {

    private static Log log = LogFactory.getLog(AuthenticationFilter.class);

    private final EntityManager entityManager;

    public AuthenticationFilter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void doFilter(ChaincodeStub stub, String[] args) throws Exception {
        if (args.length < 1) {
            throw new ChaincodeFunctionException("Argument (user) is required");
        }

        long userId = Long.valueOf(args[0]);

        User user = entityManager.get(stub, userId, User.class);
        if (user == null) {
            throw new ChaincodeFunctionException("User with id=" + userId + " is not found");
        }

        log.info("Authenticated user=" + user.getName());

        AuthenticatedUserHolder.setUser(user);
    }
}
