package br.com.desafioBack.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.desafioBack._shared.AccountTestUtil;
import br.com.desafioBack.entity.Account;
import br.com.desafioBack.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
class AccountControllerTest
{
	private final Account entity = new AccountTestUtil().createAccount();
	@Autowired
	private WebApplicationContext context;
	private MockMvc mockMvc;
	@MockBean
	private AccountService service;
	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp()
	{
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	void testFindAll() throws Exception
	{
		when(service.findAll(0, 10)).thenReturn(new PageImpl<>(Collections.singletonList(entity)));

		mockMvc.perform(get("/accounts")).andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].dueDate").value(entity.getDueDate().toString()))
			.andExpect(jsonPath("$.content[0].paymentDate").value(entity.getPaymentDate().toString()))
			.andExpect(jsonPath("$.content[0].value").value(
				Matchers.anyOf(Matchers.is(100.00), Matchers.is(100.0))))
			.andExpect(jsonPath("$.content[0].description").value(entity.getDescription()))
			.andExpect(jsonPath("$.content[0].situation").value(entity.getSituation().toString()));
	}

	@Test
	void testFindAllAccountsToPay() throws Exception
	{
		when(
			service.findAllContasAPagar(entity.getDueDate(), entity.getDescription(), 0, 10)).thenReturn(
			new PageImpl<>(Collections.singletonList(entity)));

		mockMvc.perform(get("/accounts/accountsToPay").param("description", entity.getDescription())
				.param("dueDate", entity.getDueDate().toString())).andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].dueDate").value(entity.getDueDate().toString()))
			.andExpect(jsonPath("$.content[0].paymentDate").value(entity.getPaymentDate().toString()))
			.andExpect(jsonPath("$.content[0].value").value(
				Matchers.anyOf(Matchers.is(100.00), Matchers.is(100.0))))
			.andExpect(jsonPath("$.content[0].description").value(entity.getDescription()))
			.andExpect(jsonPath("$.content[0].situation").value(entity.getSituation().toString()));
	}

	@Test
	void testCreate() throws Exception
	{
		when(service.save(entity)).thenReturn(entity);

		mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(entity))).andExpect(status().isOk())
			.andExpect(jsonPath("$.dueDate").value(entity.getDueDate().toString()))
			.andExpect(jsonPath("$.paymentDate").value(entity.getPaymentDate().toString())).andExpect(
				jsonPath("$.value").value(Matchers.anyOf(Matchers.is(100.00), Matchers.is(100.0))))
			.andExpect(jsonPath("$.description").value(entity.getDescription()))
			.andExpect(jsonPath("$.situation").value(entity.getSituation().toString()));
	}

	@Test
	void testUpdate() throws Exception
	{
		when(service.update(entity)).thenReturn(entity);

		mockMvc.perform(put("/accounts").param("id", "1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(entity))).andExpect(status().isOk())
			.andExpect(jsonPath("$.dueDate").value(entity.getDueDate().toString()))
			.andExpect(jsonPath("$.paymentDate").value(entity.getPaymentDate().toString())).andExpect(
				jsonPath("$.value").value(Matchers.anyOf(Matchers.is(100.00), Matchers.is(100.0))))
			.andExpect(jsonPath("$.description").value(entity.getDescription()))
			.andExpect(jsonPath("$.situation").value(entity.getSituation().toString()));
	}

	@Test
	void testChangeStatus() throws Exception
	{
		when(service.changeStatus(1L, entity.getSituation())).thenReturn(entity);

		mockMvc.perform(
				patch("/accounts").param("id", "1").param("situation", entity.getSituation().toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.dueDate").value(entity.getDueDate().toString()))
			.andExpect(jsonPath("$.paymentDate").value(entity.getPaymentDate().toString())).andExpect(
				jsonPath("$.value").value(Matchers.anyOf(Matchers.is(100.00), Matchers.is(100.0))))
			.andExpect(jsonPath("$.description").value(entity.getDescription()))
			.andExpect(jsonPath("$.situation").value(entity.getSituation().toString()));
	}

	@Test
	void testFindAccountById() throws Exception
	{
		when(service.findById(1L)).thenReturn(Optional.of(entity));

		mockMvc.perform(get("/accounts/1")).andExpect(status().isOk())
			.andExpect(jsonPath("$.dueDate").value(entity.getDueDate().toString()))
			.andExpect(jsonPath("$.paymentDate").value(entity.getPaymentDate().toString())).andExpect(
				jsonPath("$.value").value(Matchers.anyOf(Matchers.is(100.00), Matchers.is(100.0))))
			.andExpect(jsonPath("$.description").value(entity.getDescription()))
			.andExpect(jsonPath("$.situation").value(entity.getSituation().toString()));
	}

	@Test
	void testFindValueAmountPaid() throws Exception
	{
		when(service.findValueAmountPaid(entity.getPaymentDate().toString(),
			entity.getPaymentDate().toString())).thenReturn(entity.getValue());

		mockMvc.perform(get("/accounts/valueAmountPaid").param("paymentStartDate",
					entity.getPaymentDate().toString())
				.param("paymentEndDate", entity.getPaymentDate().toString())).andExpect(status().isOk())
			.andExpect(jsonPath("$").value(Matchers.anyOf(Matchers.is(100.00), Matchers.is(100.0))));
	}
}