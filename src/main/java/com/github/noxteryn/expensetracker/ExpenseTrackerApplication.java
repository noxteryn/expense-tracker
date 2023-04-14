package com.github.noxteryn.expensetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Create a RESTful API for managing expenses.
// Users can create, retrieve, update, and delete expense records through REST endpoints.
// The expense data can be stored in a database such as MySQL or Postgresql.
// You can implement features such as authentication and authorization to secure the API endpoints.
// After you build your application, incorporate your jar file in a docker container.
// Then invoke the exposed APIs of the container.
// You can invoke the APIs using Postman.

@SpringBootApplication
public class ExpenseTrackerApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}
}
