package com.melt.sample.bank.beans;

public class DefaultBankService implements BankService {
    private BankDao bankDao;
    private int max;
    private double tax;
    private float interest;
    private long maxMoney;

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
}
