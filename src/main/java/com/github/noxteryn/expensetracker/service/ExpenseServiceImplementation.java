package com.github.noxteryn.expensetracker.service;

import com.github.noxteryn.expensetracker.exception.ExpenseNotFoundException;
import com.github.noxteryn.expensetracker.model.Expense;
import com.github.noxteryn.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImplementation implements ExpenseService
{
	@Autowired
	private ExpenseRepository expenseRepository;

	@Override
	public Expense newExpense(Expense expense)
	{
		return expenseRepository.save(expense);
	}
	@Override
	public ResponseEntity<Expense> editExpense(Long id, Expense oldExpense, Expense newExpense)
	{
		oldExpense.setMoney(newExpense.getMoney());
		oldExpense.setDate(newExpense.getDate());
		oldExpense.setText(newExpense.getText());
		return ResponseEntity.ok(expenseRepository.save(oldExpense));
	}
	@Override
	public List<Expense> findAllExpenses()
	{
		return expenseRepository.findAll();
	}
	@Override
	public Expense findExpenseById(Long id)
	{
		Optional<Expense> expense = expenseRepository.findById(id);
		if (expense.isPresent())
		{
			return expense.get();
		}
		else
		{
			throw new ExpenseNotFoundException("Expense not found.");
		}
	}
	@Override
	public List<Expense> findByDate(LocalDate date)
	{
		return expenseRepository.findByDate(date);
	}
	@Override
	public List<Expense> findByDateRange(LocalDate startDate, LocalDate endDate)
	{
		if (endDate == null)
		{
			LocalDate currentDate = LocalDate.now();
			return expenseRepository.findByDateBetween(startDate, currentDate);
		}
		else
		{
			return expenseRepository.findByDateBetween(startDate, endDate);
		}
	}

	@Override
	public ResponseEntity<Void> deleteById(Long id)
	{
		Optional<Expense> employee = expenseRepository.findById(id);
		if (employee.isPresent())
		{
			expenseRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		else
		{
			return ResponseEntity.notFound().build();
		}
	}
}
