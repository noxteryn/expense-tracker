package com.github.noxteryn.expensetracker.controller;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.noxteryn.expensetracker.exception.ExpenseNotFoundException;
import com.github.noxteryn.expensetracker.model.Expense;
import com.github.noxteryn.expensetracker.repository.ExpenseRepository;
import com.github.noxteryn.expensetracker.service.ExpenseService;
import org.junit.Before;
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
import java.time.LocalDate;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

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

	private final Long id = 1L;
	private Expense expense;
	private String requestBody;

	@Before
	public void createExpense() throws Exception
	{
		expense = Expense.builder()
				.id(id)
				.money(BigDecimal.valueOf(19.99))
				.date(LocalDate.of(1986,3,18))
				.text("Birthday Gift!")
				.build();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		requestBody = objectMapper.writeValueAsString(expense);
	}

	// =============== GET TESTS ===============
	@Test
	@WithMockUser(username = "TestUser", roles = {"ADMIN"})
	public void test_Get_200() throws Exception
	{
		mvc.perform(get("/expense"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("[]"));
		verify(expenseService, times(1)).findAllExpenses();
	}
	@Test
	public void test_Get_WithoutLogin_401() throws Exception
	{
		mvc.perform(get("/expense"))
				.andExpect(status().isUnauthorized());
	}
	@Test
	@WithMockUser(username = "TestUser", roles = {"ADMIN"})
	public void test_Get_by_ID_200() throws Exception
	{
		when(expenseService.findExpenseById(id)).thenReturn(expense);
		mvc.perform(get("/expense/{id}", id))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(requestBody));
	}
	@Test
	@WithMockUser(username = "TestUser", roles = {"ADMIN"})
	public void test_Get_by_ID_404() throws Exception
	{
		when(expenseService.findExpenseById(anyLong())).thenThrow(ExpenseNotFoundException.class);
		mvc.perform(get("/expense/{id}", id))
				.andExpect(status().isNotFound());
	}

	// =============== POST TESTS ===============
	@Test
	@WithMockUser(username = "TestUser", roles = {"ADMIN"})
	public void test_Post_201() throws Exception
	{
		given(expenseService.newExpense(any(Expense.class)))
				.willAnswer((invocation)-> invocation.getArgument(0));
		mvc.perform(post("/expense")
						.with(csrf().asHeader()) // CSRF token is required.
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isCreated());
	}
	@Test
	public void test_Post_WithoutLogin_401() throws Exception
	{
		given(expenseService.newExpense(any(Expense.class)))
				.willAnswer((invocation)-> invocation.getArgument(0));
		mvc.perform(post("/expense")
						.with(csrf().asHeader()) // CSRF token is required.
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isUnauthorized());
	}
	@Test
	@WithMockUser(username = "TestUser", roles = {"ADMIN"})
	public void test_Post_InvalidCSRF_403() throws Exception
	{
		given(expenseService.newExpense(any(Expense.class)))
				.willAnswer((invocation)-> invocation.getArgument(0));
		mvc.perform(post("/expense")
						.with(csrf().useInvalidToken()) // Deliberately using an invalid CSRF token.
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isForbidden());
	}

	// =============== PUT TESTS ===============
	@Test
	@WithMockUser(username = "TestUser", roles = {"ADMIN"})
	public void test_Put_200() throws Exception
	{
		Expense oldExpense = expense;
		Expense newExpense = expense;

		newExpense.setText("This is a change.");
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		requestBody = objectMapper.writeValueAsString(newExpense);

		given(expenseService.findExpenseById(id)).willReturn(oldExpense);
		given(expenseService.editExpense(eq(id), eq(oldExpense), any(Expense.class)))
				.willReturn(ResponseEntity.ok(newExpense));

		mvc.perform(put("/expense/{id}", id)
					.with(csrf().asHeader()) // CSRF token is required.
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.money", is(newExpense.getMoney().toString())))
				.andExpect(jsonPath("$.date", is(newExpense.getDate().toString())))
				.andExpect(jsonPath("$.text", is(newExpense.getText())));
	}
	@Test
	public void test_Put_WithoutLogin_401() throws Exception
	{
		Expense oldExpense = expense;
		Expense newExpense = expense;

		newExpense.setText("This is a change.");
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		requestBody = objectMapper.writeValueAsString(newExpense);

		given(expenseService.findExpenseById(id)).willReturn(oldExpense);
		given(expenseService.editExpense(eq(id), eq(oldExpense), any(Expense.class)))
				.willReturn(ResponseEntity.ok(newExpense));

		mvc.perform(put("/expense/{id}", id)
						.with(csrf().asHeader()) // CSRF token is required.
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isUnauthorized());
	}
	@Test
	@WithMockUser(username = "TestUser", roles = {"ADMIN"})
	public void test_Put_InvalidCSRF_403() throws Exception
	{
		Expense oldExpense = expense;
		Expense newExpense = expense;

		newExpense.setText("This is a change.");
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		requestBody = objectMapper.writeValueAsString(newExpense);

		given(expenseService.findExpenseById(id)).willReturn(oldExpense);
		given(expenseService.editExpense(eq(id), eq(oldExpense), any(Expense.class)))
				.willReturn(ResponseEntity.ok(newExpense));

		mvc.perform(put("/expense/{id}", id)
						.with(csrf().useInvalidToken()) // Deliberately using an invalid CSRF token.
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isForbidden());
	}
	@Test
	@WithMockUser(username = "TestUser", roles = {"ADMIN"})
	public void test_Put_404() throws Exception
	{
		Expense oldExpense = expense;
		Expense newExpense = expense;

		newExpense.setText("This is a change.");
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		requestBody = objectMapper.writeValueAsString(newExpense);

		when(expenseService.findExpenseById(anyLong())).thenThrow(ExpenseNotFoundException.class);
		given(expenseService.editExpense(eq(id), eq(oldExpense), any(Expense.class)))
				.willReturn(ResponseEntity.ok(newExpense));

		mvc.perform(put("/expense/{id}", id)
						.with(csrf().asHeader()) // CSRF token is required.
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isNotFound());
	}

	// =============== DELETE TESTS ===============
	@Test
	@WithMockUser(username = "TestUser", roles = {"ADMIN"})
	public void test_Delete_204() throws Exception
	{
		when(expenseService.deleteById(id)).thenReturn(ResponseEntity.noContent().build());

		mvc.perform(delete("/expense/{id}", id)
						.with(csrf().asHeader()) // CSRF token is required.
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		verify(expenseService, times(1)).deleteById(id);
	}
	@Test
	public void test_Delete_WithoutLogin_401() throws Exception
	{
		when(expenseService.deleteById(id)).thenReturn(ResponseEntity.noContent().build());

		mvc.perform(delete("/expense/{id}", id)
						.with(csrf().asHeader()) // CSRF token is required.
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	@Test
	@WithMockUser(username = "TestUser", roles = {"ADMIN"})
	public void test_Delete__InvalidCSRF_403() throws Exception
	{
		when(expenseService.deleteById(id)).thenReturn(ResponseEntity.noContent().build());

		mvc.perform(delete("/expense/{id}", id)
						.with(csrf().useInvalidToken()) // Deliberately using an invalid CSRF token.
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}
	@Test
	@WithMockUser(username = "TestUser", roles = {"ADMIN"})
	public void test_Delete_404() throws Exception
	{
		when(expenseService.deleteById(id)).thenThrow(ExpenseNotFoundException.class);

		mvc.perform(delete("/expense/{id}", id)
						.with(csrf().asHeader()) // CSRF token is required.
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}
