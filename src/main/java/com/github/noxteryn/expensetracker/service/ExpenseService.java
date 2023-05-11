package com.github.noxteryn.expensetracker.service;

import com.github.noxteryn.expensetracker.model.Expense;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService
{
	Expense newExpense(Expense expense);
	List<Expense> findAllExpenses();
	Expense findExpenseById(Long id);
	List<Expense> findByDate(LocalDate date);
	List<Expense> findByDateRange(LocalDate startDate, LocalDate endDate);
	ResponseEntity<Expense> editExpense(Long id, Expense oldExpense, Expense newExpense);
	ResponseEntity<Void> deleteById(Long id);
}
