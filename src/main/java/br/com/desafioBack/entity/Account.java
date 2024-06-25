package br.com.desafioBack.entity;

import br.com.desafioBack.repository.Situation;
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
public class Account
{
	@javax.persistence.Id
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private LocalDate dueDate;
	private LocalDate paymentDate;
	private BigDecimal value;
	private String description;
	private Situation situation;

	@Override
	public String toString()
	{
		return "Account{" + "id=" + id + ", dueDate=" + dueDate + ", paymentDate=" + paymentDate
			+ ", value=" + value + ", description='" + description + '\'' + ", situation=" + situation
			+ '}';
	}

	public BigDecimal getValue()
	{
		return value.setScale(2, RoundingMode.HALF_UP);
	}
}