package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Represents an expense record, with amount (in dollars), date, time, category, a timeID, and a tempID
public class Expense implements Record {
    private double amount;
    private LocalDateTime dateTime;
    private ExpenseCategory category;
    private String date;
    private long timeID; //17 digits
    private int tempID;
    private int year;
    private int month;
    private int day;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter formatterWithTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public Expense(double amount) {
        this.amount = amount;

        LocalDateTime now = LocalDateTime.now();
        this.dateTime = now;
        this.date = now.format(formatter);
        this.year = now.getYear();
        this.month = now.getMonthValue();
        this.day = now.getDayOfMonth();

        String dateWithTime = now.format(formatterWithTime);
        this.timeID = Long.parseLong(dateWithTime);

        this.tempID = 0;
    }

    //MODIFIES: this
    //EFFECTS: set a tempID
    @Override
    public void setTempID(int id) {
        tempID = id;
    }

    //MODIFIES: this
    //EFFECTS: add a category to a record
    @Override
    public void classify(ExpenseCategory expenseCategory) {
        this.category = expenseCategory;
    }

    //MODIFIES: this
    //EFFECTS: reset the amount
    @Override
    public void resetAmount(double amount) {
        this.amount = amount;
    }

    //REQUIRES: the format of newDate must be yyyy-mm-dd
    //MODIFIES: this
    //EFFECTS: reset the date of transaction
    // the timeID also changes
    @Override
    public void resetDate(String newDate) {
        LocalDate newNow = LocalDate.parse(newDate, formatter);
        this.date = newNow.format(formatter);
        this.year = newNow.getYear();
        this.month = newNow.getMonthValue();
        this.day = newNow.getDayOfMonth();

        String timeIDStr = Long.toString(timeID);
        char[] timeIDCharArray = new char[17];
        timeIDCharArray = timeIDStr.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 8; i++) {
            sb.append(timeIDCharArray[8 + i]);
        }
        String lastNineDigitsStr = sb.toString();
        long lastNineDigits = Long.parseLong(lastNineDigitsStr);
        long firstEightDigits = year * 10000L + month * 100L + day;
        this.timeID = firstEightDigits * (long) Math.pow(10, 9) + lastNineDigits;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category.name();
    }

    public String getDate() {
        return date;
    }

    public int getTempID() {
        return tempID;
    }

    public long getTimeID() {
        return timeID;
    }

    public int getYear() {
        return year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
