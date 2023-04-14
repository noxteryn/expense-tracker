package com.github.noxteryn.expensetracker.service;

import com.github.noxteryn.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseService extends JpaRepository<Expense, Long>
{
}
