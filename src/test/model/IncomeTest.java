package model;

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
        assertEquals("GENERAL", inc.getCategory());
    }

    @Test
    void testReclassify() {
        inc.classify(GENERAL);
        inc.reClassify(SALARY);
        assertEquals("SALARY", inc.getCategory());
    }

}
