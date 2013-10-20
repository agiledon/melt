package com.github.agiledon.melt.sample.beans;

public interface BankService {
    void deposit(String account, double money);

    double query(String account);
}
