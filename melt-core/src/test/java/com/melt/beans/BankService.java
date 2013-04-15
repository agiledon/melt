package com.melt.beans;

public interface BankService {
    void deposit(String account, double money);

    double query(String account);
}
