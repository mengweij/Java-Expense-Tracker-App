package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.IncomeCategory.GENERAL;
import static model.IncomeCategory.SALARY;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncomeTest extends ExpenseTest{
    Income inc;

    @BeforeEach
    void setInc() {
        inc = new Income(0.00);
    }

    @Test
    void testClassify() {
        inc.classify(GENERAL);
        assertEquals("GENERAL", inc.getCategoryName());
    }

    @Test
    void testToJson() {
        inc.resetAmount(500);
        inc.classify(SALARY);
        JSONObject incjson = inc.toJson();
        String incStr = "{\"dateTime\":\"" + inc.getDateTime().toString() + "\",\"amount\":500,\"category\":\"SALARY\"}";
        assertEquals(incStr, incjson.toString());
    }
}
