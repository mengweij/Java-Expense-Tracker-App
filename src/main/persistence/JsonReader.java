package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Stream;

// Represents a reader that reads balance sheet from JSON data stored in file
public class JsonReader {
    private final String storeAddress;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    // EFFECTS: reads balance sheet from file and returns it;
    // throws IOException if an error occurs reading data from file
    public BalanceSheet read() throws IOException {
        String jsonData = readFile(storeAddress);
        JSONObject json = new JSONObject(jsonData);
        return parseBalanceSheet(json);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String storeAddress) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(storeAddress), StandardCharsets.UTF_8)) {
            stream.forEach(s -> sb.append(s));
        }

        return sb.toString();
    }

    // EFFECTS: parses balance sheet from JSON object and returns it
    private BalanceSheet parseBalanceSheet(JSONObject json) {
        BalanceSheet bs = new BalanceSheet();
        addRecords(bs, json);
        return bs;
    }

    // MODIFIES: bs
    // EFFECTS: parses records from JSON object and adds them to balance sheet
    private void addRecords(BalanceSheet bs, JSONObject json) {
        JSONArray jsonArrayExpense = json.getJSONArray("expenses");
        JSONArray jsonArrayIncome = json.getJSONArray("incomes");
        if (!jsonArrayExpense.isEmpty()) {
            for (Object i : jsonArrayExpense) {
                JSONObject nextExpense = (JSONObject) i;
                addExpense(bs, nextExpense);
            }
        }

        if (!jsonArrayIncome.isEmpty()) {
            for (Object j : jsonArrayIncome) {
                JSONObject nextIncome = (JSONObject) j;
                addIncome(bs, nextIncome);
            }
        }

    }

    // MODIFIES: bs
    // EFFECTS: parses expense from JSON object and adds it to balance sheet
    private void addExpense(BalanceSheet bs, JSONObject jsonObject) {
        double amount = jsonObject.getDouble("amount");
        CharSequence dateTimeChar = jsonObject.getString("dateTime");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeChar);
        ExpenseCategory category = ExpenseCategory.valueOf(jsonObject.getString("category"));

        Expense expense = new Expense(amount);
        expense.classify(category);
        expense.resetDateTime(dateTime);

        bs.addRecord(expense);
    }

    // MODIFIES: bs
    // EFFECTS: parses income from JSON object and adds it to balance sheet
    private void addIncome(BalanceSheet bs, JSONObject jsonObject) {
        double amount = jsonObject.getDouble("amount");
        CharSequence dateTimeChar = jsonObject.getString("dateTime");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeChar);
        IncomeCategory category = IncomeCategory.valueOf(jsonObject.getString("category"));

        Income income = new Income(amount);
        income.classify(category);
        income.resetDateTime(dateTime);

        bs.addRecord(income);
    }

}
