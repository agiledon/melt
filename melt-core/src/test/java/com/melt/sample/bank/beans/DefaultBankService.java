package com.melt.sample.bank.beans;

import java.util.List;

public class DefaultBankService implements BankService {
    private BankDao bankDao;
    private int max;
    private double tax;
    private float interest;
    private long maxMoney;
    private String account;
    private List<String> accounts;

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


    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public float getInterest() {
        return interest;
    }

    public void setInterest(float interest) {
        this.interest = interest;
    }

    public long getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(long maxMoney) {
        this.maxMoney = maxMoney;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }
}
