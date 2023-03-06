package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ExpenseTest {
    Expense ep;

    @BeforeEach
    void setEp() {
        ep = new Expense(0.00);
    }

    @Test
    void testConstructor() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterWithTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

        LocalDateTime testDateTime = ep.getDateTime();

        assertEquals(0.00, ep.getAmount());
        assertEquals(testDateTime.getYear(), ep.getYear());
        assertEquals(testDateTime.getMonthValue(), ep.getMonth());
        assertEquals(testDateTime.getDayOfMonth(), ep.getDay());
        assertEquals(formatter.format(testDateTime), ep.getDate());
        assertEquals(Long.parseLong(formatterWithTime.format(testDateTime)), ep.getTimeID());
    }

    @Test
    void testSetTempID() {
        ep.setTempID(1);
        assertEquals(1, ep.getTempID());
    }
    @Test
    void testClassify() {
        ep.classify(ExpenseCategory.GROCERY);
        assertEquals("GROCERY", ep.getCategoryName());
    }

    @Test
    void testResetAmount() {
        ep.resetAmount(100.10);
        assertEquals(100.10, ep.getAmount());
    }

    @Test
    void testResetDate() {
        ep.resetDate("2022-02-07");
        assertEquals(2022, ep.getYear());
        assertEquals(2, ep.getMonth());
        assertEquals(7, ep.getDay());

        String timeIDStr = Long.toString(ep.getTimeID());
        char[] timeIDCharArray = new char[17];
        timeIDCharArray = timeIDStr.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 8; i++) {
            sb.append(timeIDCharArray[8 + i]);
        }
        String lastNineDigitsStr = sb.toString();
        long lastNineDigits = Long.parseLong(lastNineDigitsStr);
        long newTimeID = 20220207 * (long) Math.pow(10, 9) + lastNineDigits;
        assertEquals(newTimeID, ep.getTimeID());
    }

    @Test
    void testResetDateTime() {
        LocalDateTime testDateTime = LocalDateTime.of(2010, 1, 1, 1, 0);
        ep.resetDateTime(testDateTime);
        assertEquals(2010, ep.getYear());
        assertEquals(1, ep.getMonth());
        assertEquals(1, ep.getDay());
    }

    @Test
    void testToJson() {
        ep.resetAmount(500);
        ep.classify(ExpenseCategory.HEALTH);
        JSONObject epjson = ep.toJson();
        String epStr = "{\"dateTime\":\"" + ep.getDateTime().toString() + "\",\"amount\":500,\"category\":\"HEALTH\"}";
        assertEquals(epStr, epjson.toString());

    }
}
