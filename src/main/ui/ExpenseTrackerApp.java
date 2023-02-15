package ui;

import model.*;
import model.exception.InvalidInputException;
import model.exception.NullRecordException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

//Expense tracker App
public class ExpenseTrackerApp {
    private BalanceSheet bs;
    private Scanner input;
    private YearMonth yearMonth;
    private LocalDate date;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
    private final Pattern pattern = Pattern.compile("20[0-2]\\d-(0[0-9]|1[0-2])");
    private final NumberFormat numberFormatter = new DecimalFormat("#0.00");


    // EFFECTS: run this app
    public ExpenseTrackerApp() {
        runExpenseTracker();
    }

    //MODIFIES: this
    //EFFECTS: process user input
    public void runExpenseTracker() {
        boolean keepGoing = true;
        String command;

        try {
            initialize();

            while (keepGoing) {
                displayMenu();
                command = input.next();
                command = command.toLowerCase();

                if (command.equals("q")) {
                    keepGoing = false;
                } else {
                    processMainMenu(command);
                }
            }
        } catch (InvalidInputException e) {
            System.out.println("Oops! Invalid input. Try again!");
        }

        System.out.println("\nLook forward to see your next record!");
    }

    //MODIFIES: this
    //EFFECT: initialize a balance sheet
    private void initialize() {
        bs = new BalanceSheet();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    //EFFECTS: display the main menu for users
    private void displayMenu() {
        System.out.println("\nWhat do you want to do?");
        System.out.println("\tn -> add a new transaction");
        System.out.println("\tl -> look through past records");
        System.out.println("\tq -> quit");
    }

    //MODIFIES: this
    //EFFECTS: process user's command on main menu
    private void processMainMenu(String command) throws InvalidInputException {
        if (command.equals("n")) {
            doAddRecord();
        } else if (command.equals("l")) {
            doStatistics();
        } else {
            invalidInput();
        }
    }

    //EFFECTS: return an invalid input alarm
    private void invalidInput() {
        System.out.println("Oops! Invalid input. Try again!");
    }

    //MODIFIES: this
    //EFFECTS: add a transaction record to the balance sheet
    private void doAddRecord() {
        System.out.println("\nExpense or Income?");
        System.out.println("\te -> expense");
        System.out.println("\ti -> income");

        String commandA = input.next();
        commandA = commandA.toLowerCase();

        if (commandA.equals("e")) {
            try {
                doAddExpense();
            } catch (NullRecordException e) {
                doAddRecord();
            }
        } else if (commandA.equals("i")) {
            try {
                doAddIncome();
            } catch (NullRecordException e) {
                doAddRecord();
            }
        } else {
            invalidInput();
            doAddRecord();
        }
    }

    //MODIFIES: this
    //EFFECTS: add an income record
    private void doAddIncome() throws NullRecordException {
        System.out.println("Enter how much you earn: $");

        double amount;
        try {
            amount = Double.parseDouble(input.next());

            Income newIncome = new Income(amount);
            newIncome = doClassifyIncome(newIncome);
            if (newIncome == null) {
                throw new NullRecordException();
            }

            boolean statusOfAddAmt = bs.addRecord(newIncome);
            if (statusOfAddAmt) {
                StringBuilder res = new StringBuilder();
                res.append("Successfully added! Your current balance is $");
                res.append(numberFormatter.format(bs.getBalance()));
                System.out.println(res);
            } else {
                invalidInput();
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers.");
            doAddRecord();
        }
    }

    //MODIFIES: this
    //EFFECTS: add category to the new income record
    private Income doClassifyIncome(Income newIncome) {
        System.out.println("What category is this earning? Type in one of: ");
        for (IncomeCategory i : IncomeCategory.values()) {
            System.out.println(i);
        }
        String category = input.next();
        category = category.toUpperCase();

        IncomeCategory categoryNormalized;
        try {
            categoryNormalized = IncomeCategory.valueOf(category);
            for (IncomeCategory i : IncomeCategory.values()) {
                if (i == categoryNormalized) {
                    newIncome.classify(i);
                    return newIncome;
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Not a valid category. Try again!");
        }
        return null;
    }

    //MODIFIES: this
    //EFFECTS: add an expense record
    private void doAddExpense() throws NullRecordException {
        System.out.println("Enter how much you cost: $");
    
        double amount;
        try {
            amount = Double.parseDouble(input.next());
            Expense newExpense = new Expense(amount);
            newExpense = doClassifyExpense(newExpense);
            if (newExpense == null) {
                throw new NullRecordException();
            }

            boolean statusOfAddAmt = bs.addRecord(newExpense);
            if (statusOfAddAmt) {
                StringBuilder res = new StringBuilder();
                res.append("Successfully added! Your current balance is $");
                res.append(numberFormatter.format(bs.getBalance()));
                System.out.println(res);
            } else {
                invalidInput();
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers.");
            doAddRecord();
        }
    }

    //MODIFIES: this
    //EFFECTS: add category to the new expense record
    private Expense doClassifyExpense(Expense newExpense) {
        System.out.println("What category is this expenditure? Type in one of: ");
        for (ExpenseCategory i : ExpenseCategory.values()) {
            System.out.println(i);
        }
        String category = input.next();
        category = category.toUpperCase();
        ExpenseCategory categoryNormalized;
        try {
            categoryNormalized = ExpenseCategory.valueOf(category);
            for (ExpenseCategory i : ExpenseCategory.values()) {
                if (categoryNormalized == i) {
                    newExpense.classify(i);
                    return newExpense;
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Not a valid category. Try again!");
        }
        return null;
    }

    //MODIFIES: this
    //EFFECTS: show statistic data of the balance sheet
    private void doStatistics() {
        yearMonth = YearMonth.now();
        String currentYearMonth = yearMonth.toString();

        statisticsMenu(currentYearMonth);

        String commandS;
        commandS = input.next();
        commandS = commandS.toLowerCase();

        switch (commandS) {
            case "a":
                doDisplayMonthlyRecord(currentYearMonth);
                break;
            case "m":
                doThisMonthStat(currentYearMonth);
                break;
            case "o":
                doMonthStat();
                break;
        }
    }

    //EFFECTS: display the statistics menu for users
    private void statisticsMenu(String currentYearMonth) {
        StringBuilder res = new StringBuilder();
        res.append("In this month, your total balance is $");
        res.append(numberFormatter.format(bs.totalBalanceByMonth(currentYearMonth)));
        res.append(":\n\tTotal expense: ");
        res.append(numberFormatter.format(bs.totalExpenseByMonth(currentYearMonth)));
        res.append("\n\tTotal income: ");
        res.append(numberFormatter.format(bs.totalIncomeByMonth(currentYearMonth)));
        System.out.println(res);

        System.out.println("\nSee all records of this month? -> a");
        System.out.println("Know more about this month? -> m");
        System.out.println("Look at other month? -> o");
        System.out.println("Any other keys back to main menu.");
    }

    //MODIFIES: this
    //EFFECTS: show statistic data of current month
    private void doThisMonthStat(String currentYearMonth) {
        StringBuilder res = new StringBuilder();

        date = LocalDate.now();
        int daysPast = date.getDayOfMonth();
        double averageExp = bs.totalExpenseByMonth(currentYearMonth) / daysPast;
        double averageInc = bs.totalIncomeByMonth(currentYearMonth) / daysPast;
        res.append("In ").append(currentYearMonth).append(", your daily average expense is ");
        res.append(numberFormatter.format(averageExp));
        res.append(";\nand the daily average income is ");
        res.append(numberFormatter.format(averageInc));
        res.append(".\n");
        if (averageExp == averageInc) {
            if (averageExp == 0) {
                res.append("No data can be analysed.");
            } else {
                res.append("Your are perfectly balanced!");
            }
        } else if (averageExp > averageInc) {
            res.append("Try to be more economic!");
        } else {
            res.append("Well done!");
        }
        System.out.println(res);
    }

    //MODIFIES: this
    //EFFECTS: show statistic data of a given month
    private void doMonthStat() {
        System.out.println("At which year and month you wanna have a look?");
        System.out.println("Type in the format of 'yyyy-mm'");

        if (input.hasNext(pattern)) {
            String commandM = input.next();
            StringBuilder res = new StringBuilder();
            res.append("In ").append(commandM).append(", your balance is ");
            res.append(numberFormatter.format(bs.totalBalanceByMonth(commandM)));
            res.append(":");
            res.append("\tTotal expense: ");
            res.append(numberFormatter.format(bs.totalExpenseByMonth(commandM)));
            res.append("\tTotal income: ");
            res.append(numberFormatter.format(bs.totalIncomeByMonth(commandM)));
            System.out.println(res);

            doDisplayMonthlyRecord(commandM);
        }
    }

    //MODIFIES: this
    //EFFECTS: display all records of a given month
    private void doDisplayMonthlyRecord(String yearAndMonth) {
        List<Record> expenseList = bs.listByMonth("expense", yearAndMonth);
        List<Record> incomeList = bs.listByMonth("income", yearAndMonth);

        doDisplayExpense(expenseList);
        doDisplayIncome(incomeList);
    }

    //MODIFIES: this
    //EFFECTS: display income records of a given month
    //TODO: sorting the list
    private void doDisplayIncome(List<Record> incomeList) {
        StringBuilder incStr = new StringBuilder();
        int orderNum = 1;

        if (incomeList.isEmpty()) {
            System.out.println("No income record.");
        } else {
            System.out.println("All income records:");
            for (Record i : incomeList) {
                incStr.append(orderNum++);
                incStr.append(". ").append(i.getDate()).append(" ");
                incStr.append(numberFormatter.format(i.getAmount()));
                incStr.append(" ").append(i.getCategory());
                incStr.append("\n");
            }
            System.out.println(incStr);
        }
    }

    //MODIFIES: this
    //EFFECTS: display expense records of a given month
    //TODO: sorting the list
    private void doDisplayExpense(List<Record> expenseList) {
        StringBuilder epStr = new StringBuilder();
        int orderNum = 1;

        if (expenseList.isEmpty()) {
            System.out.println("No expense record.");
        } else {
            System.out.println("All expense records:");
            for (Record i : expenseList) {
                epStr.append(orderNum++);
                epStr.append(". ").append(i.getDate()).append(" ");
                epStr.append(numberFormatter.format(i.getAmount()));
                epStr.append(" ").append(i.getCategory());
                epStr.append("\n");
            }
            System.out.println(epStr);
        }
    }

}
