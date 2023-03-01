package persistence;

import model.BalanceSheet;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
}
