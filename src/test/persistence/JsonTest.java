package persistence;

import model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    void testExpense(double amount, String category, int year, Record expense) {

        assertEquals(amount, expense.getAmount());
        assertEquals(category, expense.getCategoryName());
        assertEquals(year, expense.getYear());

    }

    void testIncome(double amount, String category, int year, Record income) {

        assertEquals(amount, income.getAmount());
        assertEquals(category, income.getCategoryName());
        assertEquals(year, income.getYear());

    }
}
