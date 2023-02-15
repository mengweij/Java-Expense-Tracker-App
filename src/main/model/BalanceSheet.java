package model;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BalanceSheet {
    private final List<Record> expenseList;
    private final List<Record> incomeList;
    private int numOfRecords;
    private double balance;
    private double totalExpense;
    private double totalIncome;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    //EFFECTS: construct a balance sheet without any record of expense
    // the initial balance is 0.00
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
    //TODO: any situation to return false?
    public boolean addRecord(Record record) {
        if (record.getClass() == Expense.class) {
            expenseList.add(record);
            totalExpense += ((Expense) record).getAmount();
        }
        if (record.getClass() == Income.class) {
            incomeList.add(record);
            totalIncome += ((Income) record).getAmount();
        }
        numOfRecords += 1;
        balance = totalIncome - totalExpense;
        return true;
    }

    //REQUIRES: numOfRecords > 0
    //EFFECTS: return the record by its timeID
    public Record fetchRecord(long id) {
        for (int i = 0; i < expenseList.size(); i++) {
            Expense temp = (Expense) expenseList.get(i);
            if (temp.getTimeID() == id) {
                return temp;
            }
        }
        for (int i = 0; i < incomeList.size(); i++) {
            Income temp = (Income) incomeList.get(i);
            if (temp.getTimeID() == id) {
                return temp;
            }
        }
        return null;
    }

    //MODIFIES: this
    //EFFECTS: delete one expense or income record from the balance sheet
    // return true if deleting successfully
    public boolean deleteRecord(Record record) {
        if (record.getClass() == Expense.class) {
            expenseList.remove(record);
        } else if (record.getClass() == Income.class) {
            incomeList.remove(record);
        }
        return true;
    }

    //TODO: improve the algorithm maybe
    //REQUIRES: input must be in the format of yyyy-mm
    //EFFECTS: return a list of expense or income of a given month and year
    public List<Record> listByMonth(String className, String yyyymm) {
        List<Record> res = new ArrayList<>();

        YearMonth callMonth = YearMonth.parse(yyyymm, formatter);
        int year = callMonth.getYear();
        int month = callMonth.getMonthValue();

        if (className.equals("expense")) {
            for (int i = 0; i < expenseList.size(); i++) {
                if (expenseList.get(i).getMonth() == month && expenseList.get(i).getYear() == year) {
                    res.add(expenseList.get(i));
                }
            }
        } else if (className.equals("income")) {
            for (int i = 0; i < incomeList.size(); i++) {
                if (incomeList.get(i).getMonth() == month && incomeList.get(i).getYear() == year) {
                    res.add(incomeList.get(i));
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


}
