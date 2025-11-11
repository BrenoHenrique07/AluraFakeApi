package br.com.alura.AluraFake.domain.task.repository;

import br.com.alura.AluraFake.domain.task.model.MultipleChoiceOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultipleChoiceOptionRepository extends JpaRepository<MultipleChoiceOption, Long> {
}
