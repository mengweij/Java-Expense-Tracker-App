package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Expense implements Record {
    private double amount;
    private ExpenseCategory category;
    private String date;
    private long timeID;
    private int year;
    private int month;
    private int day;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter formatterWithTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public Expense(double amount) {
        this.amount = amount;
        //this.category = null;
        //this.timeID = (int) new Date().getTime() / 1000;

        LocalDateTime now = LocalDateTime.now();
        this.date = now.format(formatter);
        String dateWithTime = now.format(formatterWithTime);
        this.timeID = Long.parseLong(dateWithTime);
        this.year = now.getYear();
        this.month = now.getMonthValue();
        this.day = now.getDayOfMonth();
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

    //MODIFIES: this
    //EFFECTS: change the category
    @Override
    public void reclassify(ExpenseCategory expenseCategory) {
        this.category = expenseCategory;
    }

    //REQUIRES: the format of newDate must be yyyy-mm-dd
    //MODIFIES: this
    //EFFECTS: reset the date of transaction
    @Override
    //TODO: should the timeID be changed at the same time?
    public void resetDate(String newDate) {
        LocalDate newNow = LocalDate.parse(newDate, formatter);
        this.date = newNow.format(formatter);
        this.year = newNow.getYear();
        this.month = newNow.getMonthValue();
        this.day = newNow.getDayOfMonth();
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
}
