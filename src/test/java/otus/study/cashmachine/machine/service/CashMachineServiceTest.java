package otus.study.cashmachine.machine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import otus.study.cashmachine.bank.dao.CardsDao;
import otus.study.cashmachine.bank.data.Card;
import otus.study.cashmachine.bank.service.AccountService;
import otus.study.cashmachine.bank.service.impl.CardServiceImpl;
import otus.study.cashmachine.machine.data.CashMachine;
import otus.study.cashmachine.machine.data.MoneyBox;
import otus.study.cashmachine.machine.service.impl.CashMachineServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CashMachineServiceTest {

    @Spy
    @InjectMocks
    private CardServiceImpl cardService;

    @Mock
    private CardsDao cardsDao;

    @Mock
    private AccountService accountService;

    @Mock
    private MoneyBoxService moneyBoxService;

    private CashMachineServiceImpl cashMachineService;

    private CashMachine cashMachine = new CashMachine(new MoneyBox());

    @BeforeEach
    void init() {
        cashMachineService = new CashMachineServiceImpl(cardService, accountService, moneyBoxService);
    }

    @Test
    void getMoney() {
        String cardNumber = "1234";
        String cardPin = "4321";
        BigDecimal currencyToGet = new BigDecimal(100);

        doReturn(BigDecimal.ZERO).when(cardService).getMoney(cardNumber, cardPin, currencyToGet);
        when(moneyBoxService.getMoney(any(), anyInt())).thenReturn(List.of(1, 1, 1, 1));

        List<Integer> moneyResult = cashMachineService.getMoney(cashMachine, cardNumber, cardPin, currencyToGet);

        assertEquals(List.of(1,1,1,1), moneyResult);

        assertThrows(RuntimeException.class, () -> cashMachineService.getMoney(
                cashMachine,
                "12381273891",
                cardPin,
                currencyToGet
        ));
    }

    @Test
    void putMoney() {
        String cardNumber = "1234";
        String cardPin = "4321";
        List<Integer> notes = List.of(1,1,1,1);
        BigDecimal sumToPut = new BigDecimal(6600);
        doReturn(BigDecimal.ZERO).when(cardService).getBalance(cardNumber, cardPin);
        doReturn(sumToPut).when(cardService).putMoney(cardNumber, cardPin, sumToPut);
        BigDecimal result = cashMachineService.putMoney(cashMachine, cardNumber, cardPin, notes);
        assertEquals(sumToPut, result);
    }

    @Test
    void checkBalance() {

    }

    @Test
    void changePin() {
        String cardNumber = "123456789";
        String oldPin = "1234";
        String newPin = "213123";
        doReturn(true)
                .when(cardService)
                .changePin(
                    argThat(cardNumber::equals),
                    argThat(oldPin::equals),
                    argThat(newPin::equals)
                );

        assertTrue(cashMachineService.changePin(cardNumber, oldPin, newPin));
    }

    @Test
    void changePinWithAnswer() {
        String cardNumber = "123456789";
        String oldPin = "1234";
        String newPin = "213123";
        doReturn(true)
                .when(cardService)
                .changePin(cardNumber, oldPin, newPin);

        assertTrue(cashMachineService.changePin(cardNumber, oldPin, newPin));
    }
}