package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpenseTest {
    Expense ep;

    @BeforeEach
    void setEp() {
        ep = new Expense(0.00);
    }

    @Test
    //TODO: find the best way to test the real time
    void testConstructor() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterWithTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        LocalDateTime testNow = LocalDateTime.now();

        String dateWithTime = testNow.format(formatterWithTime);
        assertEquals(0.00, ep.getAmount());
        assertEquals(testNow.getYear(), ep.getYear());
        assertEquals(testNow.getMonthValue(), ep.getMonth());
        assertEquals(testNow.getDayOfMonth(), ep.getDay());
        assertEquals(formatter.format(testNow), ep.getDate());
        assertEquals(Long.parseLong(formatterWithTime.format(testNow)), ep.getTimeID());
    }

    @Test
    void testClassify() {
        ep.classify(ExpenseCategory.GROCERY);
        assertEquals("grocery", ep.getCategory());
    }

    @Test
    void testResetAmount() {
        ep.resetAmount(100.10);
        assertEquals(100.10, ep.getAmount());
    }

    @Test
    void testReclassify() {
        ep.classify(ExpenseCategory.GROCERY);
        ep.reclassify(ExpenseCategory.FOOD);
        assertEquals("food", ep.getCategory());
    }

    @Test
    void testResetDate() {
        ep.resetDate("2022-02-07");
        assertEquals(2022, ep.getYear());
        assertEquals(2, ep.getMonth());
        assertEquals(7, ep.getDay());
    }
}
