package model;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

// Represents an expense record, with amount (in dollars), date, time, category, a timeID, and a tempID
public class Expense implements Record {
    private double amount;
    private LocalDateTime dateTime;
    private ExpenseCategory category;
    private long timeID; //17 digits
    private int tempID;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter formatterWithTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public Expense(double amount) {
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
        String dateWithTime = dateTime.format(formatterWithTime);
        this.timeID = Long.parseLong(dateWithTime);
        this.tempID = 0;
    }

    // MODIFIES: this
    // EFFECTS: sets a tempID
    @Override
    public void setTempID(int id) {
        tempID = id;
    }

    // MODIFIES: this
    // EFFECTS: adds a category to a record
    @Override
    public void classify(ExpenseCategory expenseCategory) {
        this.category = expenseCategory;
    }

    // MODIFIES: this
    // EFFECTS: resets the amount
    @Override
    public void resetAmount(double amount) {
        this.amount = amount;
    }

    // REQUIRES: the format of newDate must be yyyy-mm-dd
    // MODIFIES: this
    // EFFECTS: resets the date of transaction
    // the timeID also changes
    @Override
    public void resetDate(String date) {
        LocalDate newDate = LocalDate.parse(date, formatter);
        LocalTime prevTime = dateTime.toLocalTime();
        this.dateTime = LocalDateTime.of(newDate, prevTime);

        String timeIDStr = Long.toString(timeID);
        char[] timeIDCharArray = new char[17];
        timeIDCharArray = timeIDStr.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 8; i++) {
            sb.append(timeIDCharArray[8 + i]);
        }
        String lastNineDigitsStr = sb.toString();
        long lastNineDigits = Long.parseLong(lastNineDigitsStr);
        long firstEightDigits = getYear() * 10000L + getMonth() * 100L + getDay();
        this.timeID = firstEightDigits * (long) Math.pow(10, 9) + lastNineDigits;
    }

    public void resetDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", amount);
        jsonObject.put("category", category);
        jsonObject.put("dateTime", dateTime.toString());
        return jsonObject;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategoryName() {
        return category.name();
    }

    public String getDate() {
        return dateTime.format(formatter);
    }

    public int getTempID() {
        return tempID;
    }

    public long getTimeID() {
        return timeID;
    }

    public int getYear() {
        return dateTime.getYear();
    }

    public int getDay() {
        return dateTime.getDayOfMonth();
    }

    public int getMonth() {
        return dateTime.getMonthValue();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
    
}
