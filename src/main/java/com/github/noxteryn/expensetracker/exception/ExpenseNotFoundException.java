package com.github.noxteryn.expensetracker.exception;

public class ExpenseNotFoundException extends RuntimeException
{
	public ExpenseNotFoundException(String exception)
	{
		super (exception);
	}
}
