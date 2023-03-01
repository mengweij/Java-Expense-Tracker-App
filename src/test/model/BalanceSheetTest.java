package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BalanceSheetTest {
    BalanceSheet testbs;
    Expense ep;

    @BeforeEach
    void setUp () {
        testbs = new BalanceSheet();
        ep = new Expense(10);
    }

    @Test
    void testConstructor() {
        assertEquals(0.00, testbs.calBalance());
        assertEquals(0, testbs.calNumOfRecords());
        assertEquals(0.00, testbs.calTotalExpense());
        assertEquals(0.00, testbs.calTotalIncome());
    }

    @Test
    void testAddRecordOfExpense() {
        //Expense ep = new Expense(10);
        testbs.addRecord(ep);
        assertEquals(-10.00, testbs.calBalance());
        assertEquals(1, testbs.calNumOfRecords());
        assertEquals(10.00, testbs.calTotalExpense());
        assertEquals(0.00, testbs.calTotalIncome());
    }

    @Test
    void testAddRecordOfIncome() {
        Income inc = new Income(10.00);
        testbs.addRecord(inc);
        assertEquals(10.00, testbs.calBalance());
        assertEquals(1, testbs.calNumOfRecords());
        assertEquals(0.00, testbs.calTotalExpense());
        assertEquals(10.00, testbs.calTotalIncome());
    }

    @Test
    void testAddRecordOfIncomeNExpense() {
        Income inc = new Income(10.00);
        Expense ep = new Expense(5.00);
        testbs.addRecord(ep);
        testbs.addRecord(inc);
        assertEquals(5.00, testbs.calBalance());
        assertEquals(2, testbs.calNumOfRecords());
        assertEquals(5.00, testbs.calTotalExpense());
        assertEquals(10.00, testbs.calTotalIncome());
    }

    @Test
    void testFetchIncome() {
        Record inc = new Income(10.00);
        inc.setTempID(1);
        testbs.addRecord(inc);
        Record testInc = testbs.fetchIncome(1);
        assertTrue(testbs.getIncomeList().contains(testInc));
    }

    @Test
    void testFetchIncomeAtEndOfArray() {
        Income inc1 = new Income(100);
        Income inc2 = new Income(1000);
        inc1.setTempID(2);
        inc2.setTempID(4);
        testbs.addRecord(inc1);
        testbs.addRecord(inc2);

        Record testInc = testbs.fetchIncome(4);
        assertTrue(testbs.getIncomeList().contains(testInc));
    }

    @Test
    void testFetchIncomeNull() {
        Record nullInc = testbs.fetchIncome(1);
        assertNull(nullInc);
    }

    @Test
    void testFetchExpense() {
        Expense ep1 = new Expense(5);
        ep1.setTempID(100);
        testbs.addRecord(ep1);
        Record testEp1 = testbs.fetchExpense(100);
        assertTrue(testbs.getExpenseList().contains(testEp1));
    }

    @Test
    void testFetchExpenseNull() {
        Record nullEp = testbs.fetchExpense(2);
        assertNull(nullEp);
    }

    @Test
    void testFetchExpenseAtEndOfArray() {
        Expense ep1 = new Expense(5);
        Expense ep2 = new Expense(10);
        Expense ep3 = new Expense(100);
        ep1.setTempID(0);
        ep2.setTempID(1);
        ep3.setTempID(2);
        testbs.addRecord(ep1);
        testbs.addRecord(ep2);
        testbs.addRecord(ep3);

        Record testEp = testbs.fetchExpense(2);
        assertTrue(testbs.getExpenseList().contains(testEp));
    }
    @Test
    void testDeleteRecordOfIncome() {
        Income inc = new Income(10.00);
        testbs.addRecord(inc);

        testbs.deleteRecord(inc);
        assertEquals(0, testbs.calNumOfRecords());
    }

    @Test
    void testDeleteRecordOfExpense() {
        Expense ep = new Expense(10.00);
        testbs.addRecord(ep);

        testbs.deleteRecord(ep);
        assertEquals(0, testbs.calNumOfRecords());
    }

    @Test
    void testListByMonthExpense() {
        Expense ep1 = new Expense(5);
        Expense ep2 = new Expense(10);
        Expense ep3 = new Expense(100);
        ep1.resetDate("2023-02-01");
        ep2.resetDate("2023-02-01");
        ep3.resetDate("2023-02-01");

        testbs.addRecord(ep1);
        testbs.addRecord(ep2);
        testbs.addRecord(ep3);

        List<Record> test = new ArrayList<>();
        List<Record> nullList = new ArrayList<>();
        test.add(ep1);
        test.add(ep2);
        test.add(ep3);
        assertEquals(test, testbs.listByMonth("expense", "2023-02"));
        assertEquals(nullList, testbs.listByMonth("expense", "2023-01"));
        assertEquals(nullList, testbs.listByMonth("expense", "2022-02"));
        assertEquals(nullList, testbs.listByMonth("expense", "2022-01"));
    }

    @Test
    void testListByMonthIncome() {
        Income inc1 = new Income(100);
        Income inc2 = new Income(1000);
        inc1.resetDate("2023-02-22");
        inc2.resetDate("2023-02-02");
        testbs.addRecord(inc1);
        testbs.addRecord(inc2);

        List<Record> test = new ArrayList<>();
        List<Record> nullList = new ArrayList<>();
        test.add(inc1);
        test.add(inc2);
        assertEquals(test, testbs.listByMonth("income", "2023-02"));
        assertEquals(nullList, testbs.listByMonth("income", "2023-01"));
        assertEquals(nullList, testbs.listByMonth("income", "2022-02"));
        assertEquals(nullList, testbs.listByMonth("income", "2022-01"));
    }


    @Test
    void testTotalExpenseByMonth() {
        Expense ep1 = new Expense(5);
        Expense ep2 = new Expense(10);
        Expense ep3 = new Expense(100);
        ep1.resetDate("2023-02-01");
        ep2.resetDate("2023-02-01");
        ep3.resetDate("2023-02-01");
        testbs.addRecord(ep1);
        testbs.addRecord(ep2);
        testbs.addRecord(ep3);

        Income inc1 = new Income(100);
        Income inc2 = new Income(1000);
        inc1.resetDate("2023-02-22");
        inc2.resetDate("2023-02-02");

        testbs.addRecord(inc1);
        testbs.addRecord(inc2);

        assertEquals(115, testbs.totalExpenseByMonth("2023-02"));
    }

    @Test
    void testTotalIncomeByMonth() {
        Expense ep1 = new Expense(5);
        Expense ep2 = new Expense(10);
        Expense ep3 = new Expense(100);
        ep1.resetDate("2023-02-01");
        ep2.resetDate("2023-02-01");
        ep3.resetDate("2023-02-01");

        testbs.addRecord(ep1);
        testbs.addRecord(ep2);
        testbs.addRecord(ep3);

        Income inc1 = new Income(100);
        Income inc2 = new Income(1000);
        inc1.resetDate("2023-02-22");
        inc2.resetDate("2023-02-02");

        testbs.addRecord(inc1);
        testbs.addRecord(inc2);

        assertEquals(1100, testbs.totalIncomeByMonth("2023-02"));
    }

    @Test
    void testTotalBalanceByMonth() {
        Expense ep1 = new Expense(5);
        Expense ep2 = new Expense(10);
        Expense ep3 = new Expense(100);
        ep1.resetDate("2023-02-01");
        ep2.resetDate("2023-02-01");
        ep3.resetDate("2023-02-01");

        testbs.addRecord(ep1);
        testbs.addRecord(ep2);
        testbs.addRecord(ep3);

        Income inc1 = new Income(100);
        Income inc2 = new Income(1000);
        inc1.resetDate("2023-02-22");
        inc2.resetDate("2023-02-02");

        testbs.addRecord(inc1);
        testbs.addRecord(inc2);

        assertEquals(1100 - 115, testbs.totalBalanceByMonth("2023-02"));
    }

    @Test
    void testCalTotalExpense() {
        Expense ep1 = new Expense(5);
        Expense ep2 = new Expense(10);
        Expense ep3 = new Expense(100);
        testbs.addRecord(ep1);
        testbs.addRecord(ep2);
        testbs.addRecord(ep3);
        assertEquals(5 + 10 + 100, testbs.calTotalExpense());
    }

    @Test
    void testCalTotalIncome() {
        Income inc1 = new Income(100);
        Income inc2 = new Income(1000);
        testbs.addRecord(inc1);
        testbs.addRecord(inc2);
        assertEquals(100 + 1000, testbs.calTotalIncome());
    }

    @Test
    void testCalBalance() {
        Expense ep1 = new Expense(50);
        Income inc1 = new Income(100);
        testbs.addRecord(ep1);
        testbs.addRecord(inc1);
        assertEquals(100 - 50, testbs.calBalance());
    }

    @Test
    void testCalNumOfRecords() {
        Expense ep1 = new Expense(50);
        Income inc1 = new Income(100);
        testbs.addRecord(ep1);
        testbs.addRecord(inc1);
        assertEquals(2, testbs.calNumOfRecords());
    }
}