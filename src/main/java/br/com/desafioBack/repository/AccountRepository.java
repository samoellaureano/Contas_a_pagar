package br.com.desafioBack.repository;

import br.com.desafioBack.entity.Account;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>
{
	Page<Account> findAll(Pageable pageable);

	List<Account> findAllBySituationEqualsAndPaymentDateIsAfterAndPaymentDateBefore(Situation situation,
		LocalDate startDate, LocalDate endDate);

	Page<Account> findAllByDueDateBeforeAndDescriptionContainingIgnoreCaseAndSituationEquals(
		LocalDate dueDate, String description, Situation situation, Pageable pageable);
}