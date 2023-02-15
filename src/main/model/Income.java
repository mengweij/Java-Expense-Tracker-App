package model;

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

    //MODIFIES: this
    //EFFECTS: change the category
    public void reClassify(IncomeCategory incomeCategory) {
        this.category = incomeCategory;
    }

    @Override
    public String getCategory() {
        return category.name();
    }
}
