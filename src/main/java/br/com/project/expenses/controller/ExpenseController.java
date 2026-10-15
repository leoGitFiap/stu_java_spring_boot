package br.com.project.expenses.controller;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.project.expenses.repository.ExpenseRepository;
import jakarta.validation.Valid;
import br.com.project.expenses.model.ExpenseModel;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import br.com.project.expenses.services.ExpenseService;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    // ALTERADO: campos final, injetados pelo construtor
    private final ExpenseRepository expenseRepository;
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseRepository expenseRepository, ExpenseService expenseService) {
        this.expenseRepository = expenseRepository;
        this.expenseService = expenseService;
    }

    // ALTERADO: getter do repositório removido

    @GetMapping
    public List<ExpenseModel> getAllExpense() {
        return expenseService.readAllExpenses();
    }

    // ALTERADO: caminho "/total" e retorno BigDecimal
    @GetMapping("/total")
    public BigDecimal getTotalExpense() {
        return expenseRepository.sumTotal();
    }

    @GetMapping("/{id}")
    public ExpenseModel getExpense(@PathVariable("id") Long id) {
        return expenseService.readExpenseById(id);
    }

    @PostMapping
    public ResponseEntity<ExpenseModel> postExpense(@Valid @RequestBody ExpenseModel expenseModel) {
        ExpenseModel expense = expenseService.addExpense(expenseModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(expense); // ALTERADO: retorna o objeto salvo
    }

    // ALTERADO: "expenses/{id}" virou "/{id}"
    @PutMapping("/{id}")
    public ExpenseModel putExpense(@PathVariable("id") Long id, @Valid @RequestBody ExpenseModel expense) {
        return expenseService.updateExpense(id, expense);
    }

    // ALTERADO: "/expense/{id}" virou "/{id}", e foi adicionado o @PathVariable
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("id") Long id) {
        expenseService.deleteExpenseById(id);
        return ResponseEntity.noContent().build();
    }
}
