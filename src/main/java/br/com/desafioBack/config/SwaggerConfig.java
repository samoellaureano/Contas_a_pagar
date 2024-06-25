package br.com.desafioBack.config;

import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig
{
	@Bean
	public Docket api()
	{
		return new Docket(DocumentationType.SWAGGER_2).select()
			.apis(RequestHandlerSelectors.basePackage("br.com.desafioBack")).paths(PathSelectors.any())
			.build().useDefaultResponseMessages(false)
			.globalResponseMessage(RequestMethod.GET, responseMessageForGET())
			.globalResponseMessage(RequestMethod.POST, responseMessageForPOST())
			.globalResponseMessage(RequestMethod.PUT, responseMessageForPUT())
			.globalResponseMessage(RequestMethod.PATCH, responseMessageForPATCH())
			.securityContexts(Collections.singletonList(securityContext())).apiInfo(apiInfo())
			.securitySchemes(List.of(apiKey())).tags(new Tag("Contas", "Gerenciamento de contas"));
	}

	private ApiInfo apiInfo()
	{
		return new ApiInfoBuilder().title("Desafio Back")
			.description("\"Documentação da API de acesso aos endpoints do Desafio Back\"")
			.version("1.0.0").contact(
				new Contact("Samoel Laureano Angélica", "https://github.com/samoellaureano",
					"samoellaureano@gmail.com")).build();
	}

	private List<ResponseMessage> responseMessageForGET()
	{
		return Collections.singletonList(
			new ResponseMessageBuilder().code(500).message("Internal Server Error").build());
	}

	private List<ResponseMessage> responseMessageForPOST()
	{
		return Collections.singletonList(
			new ResponseMessageBuilder().code(500).message("Internal Server Error").build());
	}

	private List<ResponseMessage> responseMessageForPUT()
	{
		return Collections.singletonList(
			new ResponseMessageBuilder().code(500).message("Error to update").build());
	}

	private List<ResponseMessage> responseMessageForPATCH()
	{
		return Collections.singletonList(
			new ResponseMessageBuilder().code(500).message("Error to change proprietiers").build());
	}

	private SecurityContext securityContext()
	{
		return SecurityContext.builder().securityReferences(defaultAuth())
			.forPaths(PathSelectors.ant("/api/**")).build();
	}

	private ApiKey apiKey()
	{
		return new ApiKey("Token Access", "Authorization", "header");
	}

	List<SecurityReference> defaultAuth()
	{
		AuthorizationScope authorizationScope = new AuthorizationScope("ADMIN", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return List.of(new SecurityReference("Token Access", authorizationScopes));
	}
}