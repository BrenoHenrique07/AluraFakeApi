package br.com.alura.AluraFake.domain.task.repository;

import br.com.alura.AluraFake.domain.task.model.SingleChoiceOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SingleChoiceOptionRepository extends JpaRepository<SingleChoiceOption, Long> {
}
