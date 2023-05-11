package com.github.noxteryn.expensetracker.repository;

import com.github.noxteryn.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>
{
	List<Expense> findByDate(LocalDate date);
	List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
