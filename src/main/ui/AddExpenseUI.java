package ui;

import model.BalanceSheet;
import model.Expense;
import model.ExpenseCategory;
import ui.exception.InvalidInputException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeParseException;

/**
 * Represents visual user interface for adding new expense.
 */
public class AddExpenseUI extends JInternalFrame {
    private JLabel amountLabel;
    private JLabel categoryLabel;
    private JLabel dateLabel;
    private JLabel totalLabel;
    private JLabel descriptionLabel;
    private JTextField amountField;
    private JTextField dateField;
    private JButton addButton;
    private JList<String> expenseList;
    private DefaultListModel<String> expenseModel;
    private JComboBox<String> categoryComboBox;
    private DefaultComboBoxModel<String> categoryModel;
    private BalanceSheet bs;

    public AddExpenseUI(BalanceSheet bs, Component parent) {
        super("Add New Expense", false, true, false, false);
        this.bs = bs;

        setSize(parent.getWidth() / 2, parent.getHeight() / 2);
        setPosition(parent);
        setVisible(true);

        amountLabel = new JLabel("Amount:");
        dateLabel = new JLabel("Date (YYYY-MM-DD):");
        categoryLabel = new JLabel("Category:");
        descriptionLabel = new JLabel("NOTICE: Date field blank for today");

        amountField = new JTextField();
        dateField = new JTextField();

        addButton = new JButton(new AddExpenseAction());

        expenseModel = new DefaultListModel<>();
        expenseList = new JList<>(expenseModel);
        JScrollPane scrollPane = new JScrollPane(expenseList);

        categoryModel = new DefaultComboBoxModel<>();
        for (ExpenseCategory category : ExpenseCategory.values()) {
            categoryModel.addElement(category.name());
        }
        categoryComboBox = new JComboBox<>(categoryModel);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryComboBox);
        inputPanel.add(amountLabel);
        inputPanel.add(amountField);
        inputPanel.add(dateLabel);
        inputPanel.add(dateField);

        JPanel descriptionAndButtonPanel = new JPanel(new GridLayout(2, 1));
        descriptionAndButtonPanel.add(descriptionLabel);
        descriptionAndButtonPanel.add(addButton);

        JPanel expensePanel = new JPanel(new BorderLayout());
        expensePanel.add(scrollPane, BorderLayout.CENTER);
        totalLabel = new JLabel(String.format("Total Expense: $%.2f", bs.calTotalExpense()));
        expensePanel.add(totalLabel, BorderLayout.SOUTH);

        add(inputPanel, BorderLayout.NORTH);
        add(descriptionAndButtonPanel, BorderLayout.CENTER);
        add(expensePanel, BorderLayout.SOUTH);

    }

    /**
     * Sets the position of this add-expense UI relative to parent component
     * @param parent  the parent component
     */
    private void setPosition(Component parent) {
        setLocation(parent.getWidth() / 2, 0);
    }

    private class AddExpenseAction extends AbstractAction {
        AddExpenseAction() {
            super("Add");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String date = dateField.getText();
                String category = (String) categoryComboBox.getSelectedItem();
                if (amount > 0.0) {
                    Expense expense = new Expense(amount);
                    if (!date.equals("")) {
                        expense.resetDate(date);
                    }
                    expense.classify(ExpenseCategory.valueOf(category));
                    bs.addRecord(expense);

                    expenseModel.addElement(expense.getDate() + " - $" + amount + " - " + category);
                    totalLabel.setText(String.format("Total Expense: $%.2f", bs.calTotalExpense()));
                } else {
                    throw new InvalidInputException("Please enter a positive number.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(null, "Enter in the format of 'YYYY-MM-DD'", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                amountField.setText("");
                dateField.setText("");
            }

        }
    }
}


