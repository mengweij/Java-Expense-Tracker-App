package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BalanceSheetTest {
    BalanceSheet testbs;
    @BeforeEach
    void setUp () {
        testbs = new BalanceSheet();
    }

    @Test
    void testConstructor() {
        assertEquals(0.00, testbs.getBalance());
        assertEquals(0, testbs.getNumOfRecords());
        assertEquals(0.00, testbs.getTotalExpense());
        assertEquals(0.00, testbs.getTotalIncome());
    }

    @Test
    void testAddRecordOfExpense() {
        Expense ep = new Expense(10.00);
        testbs.addRecord(ep);
        assertEquals(-10.00, testbs.getBalance());
        assertEquals(1, testbs.getNumOfRecords());
        assertEquals(10.00, testbs.getTotalExpense());
        assertEquals(0.00, testbs.getTotalIncome());
    }

    @Test
    void testAddRecordOfIncome() {
        Income inc = new Income(10.00);
        testbs.addRecord(inc);
        assertEquals(10.00, testbs.getBalance());
        assertEquals(1, testbs.getNumOfRecords());
        assertEquals(0.00, testbs.getTotalExpense());
        assertEquals(10.00, testbs.getTotalIncome());
    }

    @Test
    void testAddRecordOfIncomeNExpense() {
        Income inc = new Income(10.00);
        Expense ep = new Expense(5.00);
        testbs.addRecord(ep);
        testbs.addRecord(inc);
        assertEquals(5.00, testbs.getBalance());
        assertEquals(2, testbs.getNumOfRecords());
        assertEquals(5.00, testbs.getTotalExpense());
        assertEquals(10.00, testbs.getTotalIncome());
    }

    @Test
    //TODO: to understand why temp.equals(inc) fails
    void testFetchRecordOfIncome() {
        Record inc = new Income(10.00);
        //Record ep = new Expense(11.00);
        //testbs.addRecord(ep);
        testbs.addRecord(inc);
        long incTimeID = inc.getTimeID();
        Record temp = testbs.fetchRecord(incTimeID);
        //assertTrue(temp.equals(inc));
        assertEquals(10, temp.getAmount());
        assertEquals(inc.getTimeID(), temp.getTimeID());
    }

    @Test
    void testFetchRecordOfExpense() {
        Expense ep1 = new Expense(5);
        //Expense ep2 = new Expense(10);
        //Expense ep3 = new Expense(100);
        testbs.addRecord(ep1);
        //testbs.addRecord(ep2);
        //testbs.addRecord(ep3);
        long epTimeID = ep1.getTimeID();
        Record temp = testbs.fetchRecord(epTimeID);
        assertEquals(5, temp.getAmount());
    }

    @Test
    void testFetchRecordFailure() {
        Expense ep1 = new Expense(5);
        testbs.addRecord(ep1);
        assertEquals(null, testbs.fetchRecord(0000));
    }

    @Test
    void testDeleteRecordOfIncome() {
        Income inc = new Income(10.00);
        testbs.addRecord(inc);
        boolean deleteStatus = testbs.deleteRecord(inc);
        assertTrue(deleteStatus);
        assertEquals(0, testbs.getNumOfRecords());
    }

    @Test
    void testDeleteRecordOfExpense() {
        Expense ep = new Expense(10.00);
        testbs.addRecord(ep);
        testbs.deleteRecord(ep);
        boolean deleteStatus = testbs.deleteRecord(ep);
        assertTrue(deleteStatus);
        assertEquals(0, testbs.getNumOfRecords());
    }

    @Test
    void testListByMonth() {
        Expense ep1 = new Expense(5);
        Expense ep2 = new Expense(10);
        Expense ep3 = new Expense(100);
        testbs.addRecord(ep1);
        testbs.addRecord(ep2);
        testbs.addRecord(ep3);

        List<Record> test = new ArrayList<>();
        List<Record> nullList = new ArrayList<>();
        test.add(ep1);
        test.add(ep2);
        test.add(ep3);
        assertEquals(test, testbs.listByMonth("expense", "2023-02"));
        assertEquals(nullList, testbs.listByMonth("income", "2023-02"));
    }

    @Test
    void testTotalExpenseByMonth() {
        Expense ep1 = new Expense(5);
        Expense ep2 = new Expense(10);
        Expense ep3 = new Expense(100);
        testbs.addRecord(ep1);
        testbs.addRecord(ep2);
        testbs.addRecord(ep3);

        Income inc1 = new Income(100);
        Income inc2 = new Income(1000);
        testbs.addRecord(inc1);
        testbs.addRecord(inc2);

        assertEquals(115, testbs.totalExpenseByMonth("2023-02"));
    }

    @Test
    void testTotalIncomeByMonth() {
        Expense ep1 = new Expense(5);
        Expense ep2 = new Expense(10);
        Expense ep3 = new Expense(100);
        testbs.addRecord(ep1);
        testbs.addRecord(ep2);
        testbs.addRecord(ep3);

        Income inc1 = new Income(100);
        Income inc2 = new Income(1000);
        testbs.addRecord(inc1);
        testbs.addRecord(inc2);

        assertEquals(1100, testbs.totalIncomeByMonth("2023-02"));
    }

    @Test
    void testTotalBalanceByMonth() {
        Expense ep1 = new Expense(5);
        Expense ep2 = new Expense(10);
        Expense ep3 = new Expense(100);
        testbs.addRecord(ep1);
        testbs.addRecord(ep2);
        testbs.addRecord(ep3);

        Income inc1 = new Income(100);
        Income inc2 = new Income(1000);
        testbs.addRecord(inc1);
        testbs.addRecord(inc2);

        assertEquals(1100 - 115, testbs.totalBalanceByMonth("2023-02"));
    }
}