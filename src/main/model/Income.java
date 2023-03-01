package model;

// Represents an income record, with amount (in dollars), date, time, category, a timeID, and a tempID
public class Income extends Expense {
    private IncomeCategory category;

    public Income(double amount) {
        super(amount);
    }

    //MODIFIES: this
    //EFFECTS: add a category to a record
    public void classify(IncomeCategory incomeCategory) {
        this.category = incomeCategory;
    }

    @Override
    public String getCategoryName() {
        return category.name();
    }
}
