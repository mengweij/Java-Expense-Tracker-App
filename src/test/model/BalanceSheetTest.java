package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BalanceSheetTest {
    BalanceSheet bs;
    Expense ep1;
    Expense ep2;
    Expense ep3;
    Income inc1;
    Income inc2;
    Income inc3;

    @BeforeEach
    void setUp () {
        bs = new BalanceSheet();

        ep1 = new Expense(5);
        ep2 = new Expense(10);
        ep3 = new Expense(100);

        inc1 = new Income(100);
        inc2 = new Income(1000);
    }

    @Test
    void testConstructor() {
        assertEquals(0.00, bs.calBalance());
        assertEquals(0, bs.calNumOfRecords());
        assertEquals(0.00, bs.calTotalExpense());
        assertEquals(0.00, bs.calTotalIncome());
    }

    @Test
    void testAddRecordOfExpense() {
        bs.addRecord(ep2);
        assertEquals(-10.00, bs.calBalance());
        assertEquals(1, bs.calNumOfRecords());
        assertEquals(10.00, bs.calTotalExpense());
        assertEquals(0.00, bs.calTotalIncome());
    }

    @Test
    void testAddRecordOfIncome() {
        bs.addRecord(inc1);
        assertEquals(100.00, bs.calBalance());
        assertEquals(1, bs.calNumOfRecords());
        assertEquals(0.00, bs.calTotalExpense());
        assertEquals(100.00, bs.calTotalIncome());
    }

    @Test
    void testAddRecordOfIncomeNExpense() {
        bs.addRecord(ep1);
        bs.addRecord(inc1);
        assertEquals(95.00, bs.calBalance());
        assertEquals(2, bs.calNumOfRecords());
        assertEquals(5.00, bs.calTotalExpense());
        assertEquals(100.00, bs.calTotalIncome());
    }

    @Test
    void testFetchIncome() {
        inc1.setTempID(1);
        bs.addRecord(inc1);
        Record testInc = bs.fetchIncome(1);
        assertTrue(bs.getIncomeList().contains(testInc));
    }

    @Test
    void testFetchIncomeAtEndOfArray() {
        inc1.setTempID(2);
        inc2.setTempID(4);
        bs.addRecord(inc1);
        bs.addRecord(inc2);

        Record testInc = bs.fetchIncome(4);
        assertTrue(bs.getIncomeList().contains(testInc));
    }

    @Test
    void testFetchIncomeNull() {
        Record nullInc = bs.fetchIncome(1);
        assertNull(nullInc);
    }

    @Test
    void testFetchExpense() {
        ep1.setTempID(100);
        bs.addRecord(ep1);
        Record testEp1 = bs.fetchExpense(100);
        assertTrue(bs.getExpenseList().contains(testEp1));
    }

    @Test
    void testFetchExpenseNull() {
        Record nullEp = bs.fetchExpense(2);
        assertNull(nullEp);
    }

    @Test
    void testFetchExpenseAtEndOfArray() {
        ep1.setTempID(0);
        ep2.setTempID(1);
        ep3.setTempID(2);
        bs.addRecord(ep1);
        bs.addRecord(ep2);
        bs.addRecord(ep3);

        Record testEp = bs.fetchExpense(2);
        assertTrue(bs.getExpenseList().contains(testEp));
    }
    @Test
    void testDeleteRecordOfIncome() {
        bs.addRecord(inc1);
        bs.deleteRecord(inc1);
        assertEquals(0, bs.calNumOfRecords());
    }

    @Test
    void testDeleteRecordOfExpense() {
        bs.addRecord(ep1);
        bs.deleteRecord(ep1);
        assertEquals(0, bs.calNumOfRecords());
    }

    @Test
    void testListByMonthExpense() {
        ep1.resetDate("2023-02-01");
        ep2.resetDate("2023-02-01");
        ep3.resetDate("2023-02-01");

        bs.addRecord(ep1);
        bs.addRecord(ep2);
        bs.addRecord(ep3);

        List<Record> test = new ArrayList<>();
        List<Record> nullList = new ArrayList<>();
        test.add(ep1);
        test.add(ep2);
        test.add(ep3);
        assertEquals(test, bs.listByMonth("expense", "2023-02"));
        assertEquals(nullList, bs.listByMonth("expense", "2023-01"));
        assertEquals(nullList, bs.listByMonth("expense", "2022-02"));
        assertEquals(nullList, bs.listByMonth("expense", "2022-01"));
    }

    @Test
    void testListByMonthIncome() {
        inc1.resetDate("2023-02-22");
        inc2.resetDate("2023-02-02");
        bs.addRecord(inc1);
        bs.addRecord(inc2);

        List<Record> test = new ArrayList<>();
        List<Record> nullList = new ArrayList<>();
        test.add(inc1);
        test.add(inc2);
        assertEquals(test, bs.listByMonth("income", "2023-02"));
        assertEquals(nullList, bs.listByMonth("income", "2023-01"));
        assertEquals(nullList, bs.listByMonth("income", "2022-02"));
        assertEquals(nullList, bs.listByMonth("income", "2022-01"));
    }


    @Test
    void testTotalExpenseByMonth() {
        ep1.resetDate("2023-02-01");
        ep2.resetDate("2023-02-01");
        ep3.resetDate("2023-02-01");
        bs.addRecord(ep1);
        bs.addRecord(ep2);
        bs.addRecord(ep3);

        assertEquals(115, bs.totalExpenseByMonth("2023-02"));
    }

    @Test
    void testTotalIncomeByMonth() {
        inc1.resetDate("2023-02-22");
        inc2.resetDate("2023-02-02");
        bs.addRecord(inc1);
        bs.addRecord(inc2);

        assertEquals(1100, bs.totalIncomeByMonth("2023-02"));
    }

    @Test
    void testTotalBalanceByMonth() {
        ep1.resetDate("2023-02-01");
        ep2.resetDate("2023-02-01");
        ep3.resetDate("2023-02-01");
        bs.addRecord(ep1);
        bs.addRecord(ep2);
        bs.addRecord(ep3);

        inc1.resetDate("2023-02-22");
        inc2.resetDate("2023-02-02");
        bs.addRecord(inc1);
        bs.addRecord(inc2);

        assertEquals(1100 - 115, bs.totalBalanceByMonth("2023-02"));
    }

    @Test
    void testCalTotalExpense() {
        bs.addRecord(ep1);
        bs.addRecord(ep2);
        bs.addRecord(ep3);
        assertEquals(5 + 10 + 100, bs.calTotalExpense());
    }

    @Test
    void testCalTotalIncome() {
        bs.addRecord(inc1);
        bs.addRecord(inc2);
        assertEquals(100 + 1000, bs.calTotalIncome());
    }

    @Test
    void testCalBalance() {
        bs.addRecord(ep1);
        bs.addRecord(inc1);
        assertEquals(100 - 5, bs.calBalance());
    }

    @Test
    void testCalNumOfRecords() {
        bs.addRecord(ep1);
        bs.addRecord(inc1);
        assertEquals(2, bs.calNumOfRecords());
    }

    @Test
    void testToJson() {
        ep1.classify(ExpenseCategory.FOOD);
        bs.addRecord(ep1);
        inc1.classify(IncomeCategory.GIFT);
        bs.addRecord(inc1);
        try {
            JSONObject testjson = bs.toJson();
            String bsStr = "{\"incomes\":[" +
                    "{\"dateTime\":\"" + inc1.getDateTime().toString() + "\",\"amount\":100,\"category\":\"GIFT\"}]," +
                    "\"expenses\":[" +
                    "{\"dateTime\":\"" + ep1.getDateTime().toString() + "\",\"amount\":5,\"category\":\"FOOD\"}]}";
            assertEquals(bsStr, testjson.toString());
        } catch (NullPointerException e) {
            fail("NullPointerException is not expected");
        }
    }

    @Test
    void testToJsonEmptyExpenses() {
        inc1.classify(IncomeCategory.GIFT);
        bs.addRecord(inc1);
        try {
            JSONObject testjson = bs.toJson();
            String bsStr = "{\"incomes\":[" +
                    "{\"dateTime\":\"" + inc1.getDateTime().toString() + "\",\"amount\":100,\"category\":\"GIFT\"}]," +
                    "\"expenses\":[]}";
            assertEquals(bsStr, testjson.toString());
        } catch (NullPointerException e) {
            fail("NullPointerException is not expected");
        }
    }

    @Test
    void testToJsonEmptyIncomes() {
        ep1.classify(ExpenseCategory.FOOD);
        bs.addRecord(ep1);
        try {
            JSONObject testjson = bs.toJson();
            String bsStr = "{\"incomes\":[]," +
                    "\"expenses\":[" +
                    "{\"dateTime\":\"" + ep1.getDateTime().toString() + "\",\"amount\":5,\"category\":\"FOOD\"}]}";
            assertEquals(bsStr, testjson.toString());
        } catch (NullPointerException e) {
            fail("NullPointerException is not expected");
        }
    }
}