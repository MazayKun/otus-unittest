package otus.study.cashmachine.bank.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import otus.study.cashmachine.bank.data.Card;

import static org.junit.jupiter.api.Assertions.*;

class CardsDaoTest {

    private CardsDao cardsDao;

    @BeforeEach
    void init() {
        cardsDao = new CardsDao();
    }

    @Test
    void saveCard() {
        Card cardToSave = new Card(0l, "1234", 0l, "8888");
        Card actualCard = cardsDao.saveCard(cardToSave);

        assertCardsFieldsExceptId(cardToSave, actualCard);

        Card replaceForActualCard = new Card(actualCard.getId(), "5678", 0l, "9999");

        actualCard = cardsDao.saveCard(replaceForActualCard);

        assertEquals(replaceForActualCard.getId(), actualCard.getId());
        assertCardsFieldsExceptId(replaceForActualCard, actualCard);

        assertThrows(IllegalStateException.class, () -> cardsDao.createCard("5678", 0l, "9999"));
    }

    @Test
    void getCard() {
        Card cardToSave = new Card(0l, "4321", 0l, "8888");
        cardsDao.saveCard(cardToSave);

        Card actualCard = cardsDao.getCardByNumber(cardToSave.getNumber());

        assertCardsFieldsExceptId(cardToSave, actualCard);
    }

    private void assertCardsFieldsExceptId(Card expectedCard, Card actualCard) {
        assertEquals(expectedCard.getNumber(), actualCard.getNumber());
        assertEquals(expectedCard.getAccountId(), actualCard.getAccountId());
        assertEquals(expectedCard.getPinCode(), actualCard.getPinCode());
    }
}