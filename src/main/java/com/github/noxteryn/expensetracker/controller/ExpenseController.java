package com.github.noxteryn.expensetracker.controller;

import com.github.noxteryn.expensetracker.exception.ExpenseNotFoundException;
import com.github.noxteryn.expensetracker.model.Expense;
import com.github.noxteryn.expensetracker.repository.ExpenseRepository;
import com.github.noxteryn.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
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
			return new ResponseEntity<>(expenseService.findExpenseById(id), HttpStatus.OK);
		}
		catch (ExpenseNotFoundException exception)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found.");
		}
	}
	@GetMapping("/expense/date/{date}")
	@ResponseStatus(HttpStatus.OK)
	public List<Expense> getExpenseByDate(@PathVariable("date") LocalDate date)
	{
		return expenseService.findByDate(date);
	}
	@GetMapping("/expense/search")
	@ResponseStatus(HttpStatus.OK)
	public List<Expense> getExpensesByDateRange(@RequestParam("startDate") LocalDate startDate, @RequestParam(value = "endDate", required = false) LocalDate endDate)
	{
		return expenseService.findByDateRange(startDate, endDate);
	}

	@PutMapping("/expense/{id}")
	public ResponseEntity<Expense> putEmployee(@PathVariable Long id, @RequestBody Expense newEmployee)
	{
		return expenseService.editExpense(id, expenseService.findExpenseById(id), newEmployee);
	}

	@DeleteMapping("/expense/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id)
	{
		return expenseService.deleteById(id);
	}

	@ExceptionHandler(ExpenseNotFoundException.class)
	public ResponseEntity<String> handleEmployeeNotFoundException(ExpenseNotFoundException exception)
	{
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}
}
