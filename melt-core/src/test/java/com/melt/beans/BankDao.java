package com.melt.beans;

public interface BankDao {
    void deposit(String account, double money);
    double query(String account);
}
