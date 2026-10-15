package br.com.project.expenses.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.project.expenses.model.ExpenseModel;

public interface ExpenseRepository extends JpaRepository<ExpenseModel, Long> {

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseModel e")
    BigDecimal sumTotal();

}
