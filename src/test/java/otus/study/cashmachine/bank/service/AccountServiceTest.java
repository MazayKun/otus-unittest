package otus.study.cashmachine.bank.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import otus.study.cashmachine.bank.dao.AccountDao;
import otus.study.cashmachine.bank.data.Account;
import otus.study.cashmachine.bank.service.impl.AccountServiceImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    AccountDao accountDao;

    @InjectMocks
    AccountServiceImpl accountServiceImpl;

    @Captor
    ArgumentCaptor<Account> accountCaptor;

    @Test
    void createAccountMock() {
        BigDecimal testAmount = new BigDecimal(100);
        when(accountDao.saveAccount(
                argThat(account -> account.getAmount().equals(testAmount))
        )).thenReturn(new Account(0l, testAmount));
        Account account = accountServiceImpl.createAccount(testAmount);
        assertEquals(testAmount, account.getAmount());
    }

    @Test
    void createAccountCaptor() {
        BigDecimal testAmount = new BigDecimal(100);
        when(accountDao.saveAccount(
                accountCaptor.capture()
        )).then(mock -> accountCaptor.getValue());
        Account account = accountServiceImpl.createAccount(testAmount);
        assertEquals(testAmount, account.getAmount());
    }

    @Test
    void getAccount() {
        Account testAccount = new Account(0l, new BigDecimal(100));
        when(accountDao.getAccount(
                eq(testAccount.getId())
        )).thenReturn(testAccount);
        Account returnedAccount = accountServiceImpl.getAccount(testAccount.getId());
        assertEquals(testAccount, returnedAccount);
    }

    @Test
    void getMoney() {
        Account testAccount = new Account(0l, new BigDecimal(100));
        when(accountDao.getAccount(
                eq(testAccount.getId())
        )).thenReturn(testAccount);

        BigDecimal currBalance = accountServiceImpl.getMoney(testAccount.getId(), testAccount.getAmount());
        assertEquals(BigDecimal.ZERO, currBalance);
    }

    @Test
    void getMoneyError() {
        Account testAccount = new Account(0l, new BigDecimal(100));
        when(accountDao.getAccount(
                eq(testAccount.getId())
        )).thenReturn(testAccount);

        Throwable cause = assertThrows(IllegalArgumentException.class,
                () -> accountServiceImpl.getMoney(testAccount.getId(), testAccount.getAmount().add(new BigDecimal(100)))
        );
        assertEquals("Not enough money", cause.getMessage());
    }

    @Test
    void putMoney() {
        Account testAccount = new Account(0l, new BigDecimal(100));
        when(accountDao.getAccount(
                eq(testAccount.getId())
        )).thenReturn(testAccount);

        BigDecimal currBalance = accountServiceImpl.putMoney(testAccount.getId(), new BigDecimal(100));
        assertEquals(new BigDecimal(200), currBalance);
    }

    @Test
    void checkBalance() {
        Account testAccount = new Account(0l, new BigDecimal(100));
        when(accountDao.getAccount(
                eq(testAccount.getId())
        )).thenReturn(testAccount);

        BigDecimal currBalance = accountServiceImpl.checkBalance(testAccount.getId());
        assertEquals(testAccount.getAmount(), currBalance);
    }
}
