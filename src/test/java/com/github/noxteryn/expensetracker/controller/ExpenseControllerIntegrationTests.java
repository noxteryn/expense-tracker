package com.github.noxteryn.expensetracker.controller;
import org.junit.Test;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ExpenseControllerIntegrationTests
{
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate rest;
	@Test
	public void test_Get_200() throws Exception
	{
		ResponseEntity<?> response = this.rest.withBasicAuth("noxteryn", "potato").getForEntity("http://localhost:" + port + "/expense", List.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
	}

	@Test
	public void test_Get_WithBadLogin_401() throws Exception
	{
		ResponseEntity<?> response = this.rest.withBasicAuth("noxteryn", "tomato").getForEntity("http://localhost:" + port + "/expense", List.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
	}

	@Test
	public void test_Get_WithoutLogin_401() throws Exception
	{
		ResponseEntity<?> response = this.rest.getForEntity("http://localhost:" + port + "/expense", List.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
	}
}
