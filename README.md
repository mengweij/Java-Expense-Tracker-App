# Java Expense Tracker

## Description

With the habit of tracking personal expenses for many years, 
I firsthand know the importance of friendly tools for people on forming this beneficial habit. 
However, although many developers have released similar products, 
their solutions are either too complex or lack key features; 
they rarely satisfy my real needs. My expense tracker application aims to fill this gap 
by providing a simple and user-friendly solution. The target users are individuals who want to record 
their daily expenses and incomes in the shortest time and look for a basic analysis of their costs 
for specific periods.

## User stories
- I want to be able to add an expense (or income) to my tracker.
- I want to be able to edit any expense (or income) records.
- I want to be able to view a list of my transactions in a specific period.
- I want to be able to see the total expense (or income) of a specific period.
- I want to be able to save my expense tracker to file.
- I want to be able to be able to load my expense tracker from file.

## In future...
I would like to refactor the relationships between the classes of Record, Expense, and Income. My original blueprint is 
that both the Expense and Income implement Record, but later on, I find out Expense and Income share most of the 
methods, so that Income extending Expense is a more efficient way. The result is an inorganic relationship. For now, I 
believe the best way is that Record could be an abstract class which implements all shared methods, and both Expense 
and Income extend it and additionally create their own methods.

The second refactoring which I would like to implement is to divide the BalanceSheet class for higher cohesion. 
BalanceSheet is the largest class in the project, and it handles about twenty methods covering various actions. 
It could be split into several classes, for example, BalanceSheetCalculator, BalanceSheetStorage, and 
BalanceSheetBuilder, according to the different responsibilities.
