package com.altoros.blockchain.demo.function;

import com.altoros.blockchain.demo.model.User;
import com.altoros.blockchain.demo.persistence.EntityManager;
import org.hyperledger.java.shim.ChaincodeStub;

import java.util.List;

/**
 * @author Nikita Gorbachevski
 */
public class GetUsersFunction implements ChaincodeFunction<List<User>> {

    private final EntityManager entityManager;

    public GetUsersFunction(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> invoke(ChaincodeStub stub, String[] args) throws Exception {
        return entityManager.getList(stub, User.class);
    }
}
