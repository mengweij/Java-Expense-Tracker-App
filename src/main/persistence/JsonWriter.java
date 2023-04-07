package persistence;

import model.BalanceSheet;
import model.Event;
import model.EventLog;
import org.json.JSONObject;

import java.io.*;

// Represents a writer that writes JSON representation of balance sheet to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String storeAddress;

    // EFFECTS: constructs writer to write to designated file
    public JsonWriter(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(storeAddress));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of balance sheet to file
    public void write(BalanceSheet bs) {
        JSONObject json = bs.toJson();
        saveToFile(json.toString(TAB));
        EventLog.getInstance().logEvent(new Event("Data saved"));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
