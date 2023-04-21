package com.github.noxteryn.expensetracker.controller;

import com.fasterxml.jackson.databind.*;
import com.github.noxteryn.expensetracker.model.Expense;
import com.github.noxteryn.expensetracker.repository.ExpenseRepository;
import com.github.noxteryn.expensetracker.service.ExpenseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.sql.Date;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ExpenseController.class)
public class ExpenseControllerUnitTest
{
	@Autowired
	private MockMvc mvc;
	@Autowired
	@MockBean
	private ExpenseRepository expenseRepository;
	@MockBean
	private ExpenseService expenseService;

	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void testPostEndpointReturns201() throws Exception // Post Request
	{
		Expense expense = new Expense(new BigDecimal("9.99"), new Date(1355270400000L), "Hello World!");
		String requestBody = new ObjectMapper().writeValueAsString(expense);

		mvc.perform(post("/expense")
			.with(csrf().asHeader()) // CSRF token is required.
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody))
			.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void testPostEndpointReturns403() throws Exception // Post Request without CSRF token
	{
		Expense expense = new Expense(new BigDecimal("9.99"), new Date(1355270400000L), "Hello World!");
		String requestBody = new ObjectMapper().writeValueAsString(expense);

		mvc.perform(post("/expense")
			.with(csrf().useInvalidToken()) // Deliberately using an invalid CSRF token.
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody))
			.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void testGetEndpointReturns200() throws Exception // Get Request
	{
		mvc.perform(get("/expense"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json("[]"));
		verify(expenseService, times(1)).findAllExpenses();
	}

	@Test
	public void testGetEndpointReturns401() throws Exception // Get Request without Login
	{
		mvc.perform(get("/expense"))
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void testGetByIdEndpointReturns302() throws Exception // Get by ID
	{
		Long id = 1L;
		Expense expense = new Expense(new BigDecimal("9.99"), new Date(1355270400000L), "Hello World!");
		String requestBody = new ObjectMapper().writeValueAsString(expense);
		given(expenseService.findExpenseById(id)).willReturn(expense);
		mvc.perform(get("/expense/1"))
			.andExpect(status().isFound())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(requestBody));
	}

	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void testDeleteEndpointReturns200() throws Exception // Delete Request
	{
		Long id = 1L;
		willDoNothing().given(expenseService).deleteById(id);
		mvc.perform(delete("/expense/{id}", id)
			.with(csrf().asHeader())) // CSRF token is required.
			.andExpect(status().isOk());
	}
}
