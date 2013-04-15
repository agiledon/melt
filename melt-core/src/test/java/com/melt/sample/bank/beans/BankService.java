package com.melt.sample.bank.beans;

public interface BankService {
    void deposit(String account, double money);

    double query(String account);
}
