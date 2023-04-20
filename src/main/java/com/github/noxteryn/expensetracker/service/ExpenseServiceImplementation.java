package com.github.noxteryn.expensetracker.service;

import com.github.noxteryn.expensetracker.exception.ExpenseNotFoundException;
import com.github.noxteryn.expensetracker.model.Expense;
import com.github.noxteryn.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImplementation implements ExpenseService
{
	@Autowired
	private ExpenseRepository expenseRepository;
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
	public List<Expense> findByDate(Date date)
	{
		return expenseRepository.findByDate(date);
	}
	public List<Expense> findByDateRange(Date startDate, Date endDate)
	{
		if (endDate == null)
		{
			LocalDate currentDate = LocalDate.now();
			Date date2 = Date.valueOf(currentDate);
			return expenseRepository.findByDateBetween(startDate, date2);
		}
		else
		{
			return expenseRepository.findByDateBetween(startDate, endDate);
		}
	}
}
