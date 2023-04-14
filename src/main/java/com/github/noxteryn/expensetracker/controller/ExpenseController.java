package com.github.noxteryn.expensetracker.controller;

import com.github.noxteryn.expensetracker.model.Expense;
import com.github.noxteryn.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class ExpenseController
{
	@Autowired
	ExpenseService expenseService;
	@PostMapping("/expense")
	@ResponseStatus(HttpStatus.CREATED)
	Expense create(@RequestBody Expense expense)
	{
		return expenseService.save(expense);
	}
	@GetMapping("/expense")
	List<Expense> read()
	{
		return expenseService.findAll();
	}
	@PutMapping("/expense")
	Expense update(@RequestBody Expense expense)
	{
		return expenseService.save(expense);
	}
	@DeleteMapping("/expense/{id}")
	void delete(@PathVariable Long id)
	{
		expenseService.deleteById(id);
	}
}
