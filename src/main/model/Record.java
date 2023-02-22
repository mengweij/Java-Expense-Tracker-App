package model;

import java.time.LocalDateTime;

// Represents a transaction record, which could be expense or income
public interface Record {

    //MODIFIES: this
    //EFFECTS: set a tempID
    void setTempID(int id);

    //MODIFIES: this
    //EFFECTS: add category to a record
    void classify(ExpenseCategory expenseCategory);

    //MODIFIES: this
    //EFFECTS: reset the amount
    void resetAmount(double amount);

    //REQUIRES: the format of newDate must be yyyy-mm-dd
    //MODIFIES: this
    //EFFECTS: reset the date of transaction
    // the timeID also changes
    void resetDate(String newDate);

    int getTempID();

    long getTimeID();

    double getAmount();

    int getMonth();

    int getYear();

    String getDate();

    String getCategory();

    LocalDateTime getDateTime();
}
