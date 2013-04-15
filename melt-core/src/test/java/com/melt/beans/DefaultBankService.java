package com.melt.beans;

public class DefaultBankService implements BankService {
    private BankDao bankDao;

    @Override
    public void deposit(String account, double money) {
        bankDao.deposit(account, money);
    }

    @Override
    public double query(String account) {
        return bankDao.query(account);
    }

    public BankDao getBankDao() {
        return bankDao;
    }

    public void setBankDao(BankDao bankDao) {
        this.bankDao = bankDao;
    }
}
