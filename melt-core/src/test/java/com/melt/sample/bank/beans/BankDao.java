package com.melt.sample.bank.beans;

public interface BankDao {
    void deposit(String account, double money);
    double query(String account);
}
