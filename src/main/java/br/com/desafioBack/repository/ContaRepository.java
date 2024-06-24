package br.com.desafioBack.repository;

import br.com.desafioBack.entity.Conta;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long>
{
	Conta findContaById(long id);

	PageImpl<Conta> findAll(Pageable pageable);

	List<Conta> findAllBySituacaoEqualsAndDataPagamentoIsAfterAndDataPagamentoBefore(Situacao situacao,
		LocalDate dataInicio, LocalDate dataFim);

	PageImpl<Conta> findAllByDataVencimentoBeforeAndDescricaoContainingIgnoreCaseAndSituacaoEquals(
		LocalDate dataVencimento, String descricao, Situacao situacao, Pageable pageable);
}