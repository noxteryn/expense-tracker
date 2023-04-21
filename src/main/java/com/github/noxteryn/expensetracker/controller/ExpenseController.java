package com.github.noxteryn.expensetracker.controller;

import com.github.noxteryn.expensetracker.exception.ExpenseNotFoundException;
import com.github.noxteryn.expensetracker.model.Expense;
import com.github.noxteryn.expensetracker.repository.ExpenseRepository;
import com.github.noxteryn.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Date;
import java.util.List;

@RestController
public class ExpenseController
{
	private ExpenseService expenseService;
	private final ExpenseRepository expenseRepository;
	public ExpenseController(ExpenseRepository expenseRepository)
	{
		this.expenseRepository = expenseRepository;
	}
	@Autowired
	public void setExpenseService(ExpenseService expenseService)
	{
		this.expenseService = expenseService;
	}

	@PostMapping("/expense")
	@ResponseStatus(HttpStatus.CREATED)
	public Expense create(@RequestBody Expense expense)
	{
		return expenseService.newExpense(expense);
	}

	@GetMapping("/expense")
	public ResponseEntity<List<Expense>> read()
	{
		List<Expense> list = expenseService.findAllExpenses();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	@GetMapping("/expense/{id}")
	public ResponseEntity<Expense> getExpenseById(@PathVariable("id") Long id)
	{
		try
		{
			return new ResponseEntity<>(expenseService.findExpenseById(id), HttpStatus.FOUND);
		}
		catch (ExpenseNotFoundException exception)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found.");
		}
	}
	@GetMapping("/expense/date/{date}")
	@ResponseStatus(HttpStatus.OK)
	public List<Expense> getExpenseByDate(@PathVariable("date") Date date)
	{
		return expenseService.findByDate(date);
	}
	@GetMapping("/expense/search")
	@ResponseStatus(HttpStatus.OK)
	public List<Expense> getExpensesByDateRange(@RequestParam("startDate") Date startDate, @RequestParam(value = "endDate", required = false) Date endDate)
	{
		return expenseService.findByDateRange(startDate, endDate);
	}

	@PutMapping("/expense")
	@ResponseStatus(HttpStatus.OK)
	public Expense update(@RequestBody Expense expense)
	{
		return expenseService.editExpense(expense);
	}

	@DeleteMapping("/expense/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") Long id)
	{
		try
		{
			expenseService.deleteById(id);
		}
		catch (ExpenseNotFoundException exception)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found.");
		}
	}
}
