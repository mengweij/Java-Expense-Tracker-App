package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class Test {
    private JFrame frame;
    private JLabel amountLabel, categoryLabel, totalLabel;
    private JFormattedTextField amountField;
    private JButton addButton, clearButton;
    private JList<String> expenseList;
    private DefaultListModel<String> expenseModel;
    private JComboBox<String> categoryComboBox;
    private DefaultComboBoxModel<String> categoryModel;
    private double totalExpense = 0.0;

    public Test() {
        frame = new JFrame("Expense Tracker");
        frame.setLayout(new BorderLayout());
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        amountLabel = new JLabel("Amount:");
        categoryLabel = new JLabel("Category:");
        amountField = new JFormattedTextField(NumberFormat.getNumberInstance());
        amountField.setColumns(10);
        addButton = new JButton("Add");
        clearButton = new JButton("Clear");
        expenseModel = new DefaultListModel<>();
        expenseList = new JList<>(expenseModel);
        JScrollPane scrollPane = new JScrollPane(expenseList);

        categoryModel = new DefaultComboBoxModel<>();
        categoryModel.addElement("Food");
        categoryModel.addElement("Entertainment");
        categoryModel.addElement("Transportation");
        categoryModel.addElement("Utilities");
        categoryComboBox = new JComboBox<>(categoryModel);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(amountLabel);
        inputPanel.add(amountField);
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryComboBox);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);

        JPanel expensePanel = new JPanel(new BorderLayout());
        expensePanel.add(scrollPane, BorderLayout.CENTER);
        totalLabel = new JLabel(String.format("Total Expense: $%.2f", totalExpense));
        expensePanel.add(totalLabel, BorderLayout.SOUTH);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(expensePanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double amount = ((Number) amountField.getValue()).doubleValue();
                String category = (String) categoryComboBox.getSelectedItem();
                if (amount > 0.0) {
                    expenseModel.addElement("$" + amount + " (" + category + ")");
                    totalExpense += amount;
                    totalLabel.setText(String.format("Total Expense: $%.2f", totalExpense));
                    amountField.setValue(null);
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                expenseModel.clear();
                totalExpense = 0.0;
                totalLabel.setText(String.format("Total Expense: $%.2f", totalExpense));
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Test ui = new Test();
        ui.show();
    }
}
