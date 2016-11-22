package com.altoros.blockchain.demo.function;

import org.hyperledger.java.shim.ChaincodeStub;

/**
 * @author Nikita Gorbachevski
 */
@FunctionalInterface
public interface ChaincodeFunction<T> {

    T invoke(ChaincodeStub stub, String[] args) throws Exception;
}
