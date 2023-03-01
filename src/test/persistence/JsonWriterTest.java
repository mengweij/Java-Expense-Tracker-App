package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    @Test
    void testInvalidAddress() {
        JsonWriter writer = new JsonWriter("./data/\0illegal.json");
        try {
            writer.open();
            fail("FileNotFoundException is expected");
        } catch (FileNotFoundException e) {
            //pass
        }
    }

    @Test
    void testWriteToEmptyBalanceSheet() {
        JsonWriter writer = new JsonWriter("./data/emptyBalanceSheet.json");
        BalanceSheet bs = new BalanceSheet();
        try {
            writer.open();
            writer.write(bs);
            writer.close();
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException is not expected");
        }

        JsonReader reader = new JsonReader("./data/emptyBalanceSheet.json");
        try {
            BalanceSheet emptybs = reader.read();
            assertEquals(0, emptybs.calNumOfRecords());
        } catch (IOException e) {
            fail("IOException is not expected");
        }
    }

    @Test
    void testWriteGeneralBalanceSheet() {
        BalanceSheet bs = new BalanceSheet();
        Expense ep1 = new Expense(50);
        Income inc1 = new Income(100);
        ep1.classify(ExpenseCategory.FOOD);
        inc1.classify(IncomeCategory.SALARY);
        bs.addRecord(ep1);
        bs.addRecord(inc1);

        try {
            JsonWriter writer = new JsonWriter("./data/generalBalanceSheet.json");
            writer.open();
            writer.write(bs);
            writer.close();
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException is not expected");
        }

        JsonReader reader = new JsonReader("./data/generalBalanceSheet.json");
        try {
            BalanceSheet generalBS = reader.read();
            List<Record> expenses = generalBS.getExpenseList();
            assertEquals(1, expenses.size());
            assertEquals(2, generalBS.calNumOfRecords());
            testExpense(50, "FOOD", 2023, generalBS.getExpenseList().get(0));
            testIncome(100, "SALARY", 2023, generalBS.getIncomeList().get(0));
        } catch (IOException E) {
            fail("IOException is not expected");
        }
    }
}
