package com.altoros.blockchain.demo.persistence;

/**
 * @author Nikita Gorbachevski
 */
public interface Persistable {

    void setId(long id);

    long getId();

    String getIdGeneratorName();
}
