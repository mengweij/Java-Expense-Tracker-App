package model;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Represents a balance sheet with both expense and income records
//   it maintains the number of records, total expense and income, and balance (in dollars)
public class BalanceSheet {
    private final List<Record> expenseList;
    private final List<Record> incomeList;
    private int numOfRecords;
    private double balance;
    private double totalExpense;
    private double totalIncome;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    public BalanceSheet() {
        expenseList = new ArrayList<>();
        incomeList = new ArrayList<>();
        numOfRecords = 0;
        balance = 0.00;
        totalExpense = 0.00;
        totalIncome = 0.00;
    }

    //MODIFIES: this
    //EFFECTS: add a record to the balance sheet
    // return true if added successfully
    public boolean addRecord(Record record) {
        if (record.getClass() == Expense.class) {
            expenseList.add(record);
            totalExpense += record.getAmount();
        }
        if (record.getClass() == Income.class) {
            incomeList.add(record);
            totalIncome += record.getAmount();
        }
        numOfRecords += 1;
        balance = totalIncome - totalExpense;
        return true;
    }

    //EFFECTS: return expense by its tempID
    //  return null if no match
    public Record fetchExpense(int id) {
        for (Record temp : expenseList) {
            if (temp.getTempID() == id) {
                return temp;
            }
        }
        return null;
    }

    //EFFECTS: return income by its tempID
    //  return null if no match
    public Record fetchIncome(int id) {
        for (Record temp : incomeList) {
            if (temp.getTempID() == id) {
                return temp;
            }
        }
        return null;
    }

    //MODIFIES: this
    //EFFECTS: delete one expense or income record from the balance sheet
    // return true if deleting successfully
    public void deleteRecord(Record record) {
        if (record.getClass() == Expense.class) {
            expenseList.remove(record);
        } else {
            incomeList.remove(record);
        }
    }

    //TODO: improve the algorithm maybe
    //REQUIRES: input must be in the format of yyyy-mm
    //EFFECTS: return a list of expense or income of a given month and year
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
        return res;
    }

    //REQUIRES: input must be in the format of yyyy-mm
    //EFFECTS: calculate the total expense of a given month and year
    public double totalExpenseByMonth(String yyyymm) {
        List<Record> list = listByMonth("expense", yyyymm);
        double total = 0.00;
        for (Record record : list) {
            total += record.getAmount();
        }
        return total;
    }

    //REQUIRES: input must be in the format of yyyy-mm
    //EFFECTS: calculate the total income of a given month and year
    public double totalIncomeByMonth(String yyyymm) {
        List<Record> list = listByMonth("income", yyyymm);
        double total = 0.00;
        for (Record record : list) {
            total += record.getAmount();
        }
        return total;
    }

    //REQUIRES: input must be in the format of yyyy-mm
    //EFFECTS: calculate the balance of a given month and year
    public double totalBalanceByMonth(String yyyymm) {
        return this.totalIncomeByMonth(yyyymm) - this.totalExpenseByMonth(yyyymm);
    }


    public double getTotalExpense() {
        return totalExpense;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getBalance() {
        return balance;
    }

    public int getNumOfRecords() {
        return expenseList.size() + incomeList.size();
    }

    public List<Record> getExpenseList() {
        return expenseList;
    }

    public List<Record> getIncomeList() {
        return incomeList;
    }

}
