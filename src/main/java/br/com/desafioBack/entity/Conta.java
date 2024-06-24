package br.com.desafioBack.entity;

import br.com.desafioBack.repository.Situacao;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Entity
@Data
public class Conta
{
	@javax.persistence.Id
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private LocalDate dataVencimento;
	private LocalDate dataPagamento;
	private BigDecimal valor;
	private String descricao;
	private Situacao situacao;

	@Override
	public String toString()
	{
		return "Conta{" + "id=" + id + ", dataVencimento=" + dataVencimento + ", dataPagamento="
			+ dataPagamento + ", valor=" + valor + ", descricao='" + descricao + '\'' + ", situacao='"
			+ situacao + '\'' + '}';
	}
	public BigDecimal getValor() {
		return valor.setScale(2, RoundingMode.HALF_UP);
	}
}