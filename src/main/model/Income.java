package model;

import org.json.JSONObject;

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

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", getAmount());
        jsonObject.put("category", category);
        jsonObject.put("dateTime", getDateTime().toString());
        return jsonObject;
    }

    @Override
    public String getCategoryName() {
        return category.name();
    }
}
