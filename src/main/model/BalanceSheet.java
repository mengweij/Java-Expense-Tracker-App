package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Represents a balance sheet with both expense and income records
//   it maintains the number of records, total expense and income, and balance (in dollars)
public class BalanceSheet {
    private final List<Record> expenseList;
    private final List<Record> incomeList;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    public BalanceSheet() {
        expenseList = new ArrayList<>();
        incomeList = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: add a record to the balance sheet
    // return true if added successfully
    public boolean addRecord(Record record) {
        if (record.getClass() == Expense.class) {
            expenseList.add(record);
            EventLog.getInstance().logEvent(new Event("Expense added to Balance Sheet"));
        }
        if (record.getClass() == Income.class) {
            incomeList.add(record);
            EventLog.getInstance().logEvent(new Event("Income added to Balance Sheet"));
        }
        return true;
    }

    // EFFECTS: return expense by its tempID
    //  return null if no match
    public Record fetchExpense(int id) {
        for (Record temp : expenseList) {
            if (temp.getTempID() == id) {
                return temp;
            }
        }
        return null;
    }

    // EFFECTS: return income by its tempID
    //  return null if no match
    public Record fetchIncome(int id) {
        for (Record temp : incomeList) {
            if (temp.getTempID() == id) {
                return temp;
            }
        }
        return null;
    }

    // MODIFIES: this
    // EFFECTS: delete one expense or income record from the balance sheet
    //  return true if deleting successfully
    public void deleteRecord(Record record) {
        if (record.getClass() == Expense.class) {
            expenseList.remove(record);
        } else {
            incomeList.remove(record);
        }
    }


    // REQUIRES: input must be in the format of yyyy-mm
    // EFFECTS: return a list of expense or income of a given month and year
    // TODO: improve the algorithm maybe
    public List<Record> listByMonth(String className, String yyyymm) {
        List<Record> res = new ArrayList<>();

        YearMonth callMonth = YearMonth.parse(yyyymm, formatter);
        int year = callMonth.getYear();
        int month = callMonth.getMonthValue();

        if ("expense".equals(className)) {
            for (Record record : expenseList) {
                if (record.getMonth() == month && record.getYear() == year) {
                    res.add(record);
                }
            }
        } else {
            for (Record record : incomeList) {
                if (record.getMonth() == month && record.getYear() == year) {
                    res.add(record);
                }
            }
        }
        EventLog.getInstance().logEvent(new Event("Records of " + yyyymm + " reviewed"));
        return res;
    }

    // REQUIRES: input must be in the format of yyyy-mm
    // EFFECTS: calculate the total expense of a given month and year
    public double totalExpenseByMonth(String yyyymm) {
        List<Record> list = listByMonth("expense", yyyymm);
        double total = 0.00;
        for (Record record : list) {
            total += record.getAmount();
        }
        return total;
    }

    // REQUIRES: input must be in the format of yyyy-mm
    // EFFECTS: calculate the total income of a given month and year
    public double totalIncomeByMonth(String yyyymm) {
        List<Record> list = listByMonth("income", yyyymm);
        double total = 0.00;
        for (Record record : list) {
            total += record.getAmount();
        }
        return total;
    }

    // REQUIRES: input must be in the format of yyyy-mm
    // EFFECTS: calculate the balance of a given month and year
    public double totalBalanceByMonth(String yyyymm) {
        return this.totalIncomeByMonth(yyyymm) - this.totalExpenseByMonth(yyyymm);
    }

    // EFFECTS: returns this as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("expenses", expenseListToJson());
        json.put("incomes", incomeListToJson());

        return json;
    }

    // EFFECTS: returns expenses in this balance sheet as a JSON array
    private JSONArray expenseListToJson() {
        JSONArray expenseJsonArray = new JSONArray();

        for (Record expense : expenseList) {
            expenseJsonArray.put(expense.toJson());
        }

        return expenseJsonArray;
    }

    // EFFECTS: returns incomes in this balance sheet as a JSON array
    private JSONArray incomeListToJson() {
        JSONArray incomeJsonArray = new JSONArray();

        for (Record income : incomeList) {
            incomeJsonArray.put(income.toJson());
        }

        return incomeJsonArray;
    }

    // EFFECTS: calculates the total expense
    public double calTotalExpense() {
        double totalExpense = 0;
        for (Record expense : expenseList) {
            totalExpense += expense.getAmount();
        }
        return totalExpense;
    }

    // EFFECTS: calculates the total expense
    public double calTotalIncome() {
        double totalIncome = 0;
        for (Record income : incomeList) {
            totalIncome += income.getAmount();
        }
        return totalIncome;
    }

    // EFFECTS: calculates the balance amount
    public double calBalance() {
        return calTotalIncome() - calTotalExpense();
    }

    // EFFECTS: calculates the number of records
    public int calNumOfRecords() {
        return expenseList.size() + incomeList.size();
    }

    public List<Record> getExpenseList() {
        return expenseList;
    }

    public List<Record> getIncomeList() {
        return incomeList;
    }

}
