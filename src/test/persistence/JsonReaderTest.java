package persistence;

import model.BalanceSheet;
import model.Record;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest{
    @Test
    void testNonExistAddress() {
        JsonReader reader = new JsonReader("./data/nonexist.json");
        try {
            BalanceSheet bs = reader.read();
            fail("IOException is expected");
        } catch (IOException E) {
            // pass
        }
    }

    @Test
    void testReadFromEmptyBalanceSheet() {
        JsonReader reader = new JsonReader("./data/emptyBalanceSheet.json");
        try {
            BalanceSheet emptybs = reader.read();
            assertEquals(0, emptybs.calNumOfRecords());
        } catch (IOException e) {
            fail("IOException is not expected");
        }
    }

    @Test
    void testReadFromGeneralBalanceSheet() {
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
