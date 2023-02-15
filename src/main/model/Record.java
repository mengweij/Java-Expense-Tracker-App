package model;

public interface Record {
    //MODIFIES: this
    //EFFECTS: add a category to a record
    void classify(ExpenseCategory expenseCategory);

    //MODIFIES: this
    //EFFECTS: reset the amount
    void resetAmount(double amount);

    //MODIFIES: this
    //EFFECTS: change the category
    void reclassify(ExpenseCategory expenseCategory);

    //REQUIRES: the format of newDate must be yyyy-mm-dd
    //MODIFIES: this
    //EFFECTS: reset the date of transaction
    void resetDate(String newDate);

    long getTimeID();

    double getAmount();

    int getMonth();

    int getYear();

    String getDate();

    String getCategory();
}
