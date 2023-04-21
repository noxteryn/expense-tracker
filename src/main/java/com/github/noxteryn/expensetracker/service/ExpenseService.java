package com.github.noxteryn.expensetracker.service;

import com.github.noxteryn.expensetracker.model.Expense;
import java.sql.Date;
import java.util.List;

public interface ExpenseService
{
	Expense newExpense(Expense expense);
	List<Expense> findAllExpenses();
	Expense findExpenseById(Long id);
	List<Expense> findByDate(Date date);
	List<Expense> findByDateRange(Date startDate, Date endDate);
	Expense editExpense(Expense expense);
	void deleteById(Long id);
}
