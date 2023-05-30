package otus.study.cashmachine.bank.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import otus.study.cashmachine.bank.data.Account;
import otus.study.cashmachine.bank.db.Accounts;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountDaoTest {

    private AccountDao accountDao;

    @BeforeEach
    void init() {
        accountDao = new AccountDao();
    }

    @Test
    void mainTest() {
        Account accountToSave = new Account(0, new BigDecimal(100));
        Account actualAccount = accountDao.saveAccount(accountToSave);
        assertEquals(accountToSave.getAmount(), Accounts.accounts.get(actualAccount.getId()).getAmount());
        assertEquals(accountToSave.getAmount(), accountDao.getAccount(actualAccount.getId()).getAmount());
        assertThrows(IllegalArgumentException.class, () -> accountDao.getAccount(-1l));
    }
}