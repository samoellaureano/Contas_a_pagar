package br.com.desafioBack;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.desafioBack.controller.ContaController;
import br.com.desafioBack.entity.Conta;
import br.com.desafioBack.services.ContaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.regex.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ContaController.class)
class ContaControllerTest
{

	private final Conta entity = new ContaTesteUtil().createConta();

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@MockBean
	private ContaService service;

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

		mockMvc.perform(get("/contas")).andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].dataVencimento").value(entity.getDataVencimento().toString()))
			.andExpect(jsonPath("$.content[0].dataPagamento").value(entity.getDataPagamento().toString()))
			.andExpect(jsonPath("$.content[0].valor").value(Matchers.anyOf(Matchers.is(100.00), Matchers.is(100.0))))
			.andExpect(jsonPath("$.content[0].descricao").value(entity.getDescricao()))
			.andExpect(jsonPath("$.content[0].situacao").value(entity.getSituacao().toString()));
	}
}


