package br.com.project.expenses.controller;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.project.expenses.repository.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
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
@RequestMapping("/expenses")
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
    public ResponseEntity<Object> getExpense(@PathVariable("id") Long id) {
        try {
            ExpenseModel expenseModel = expenseService.readExpenseById(id);
            return new ResponseEntity<>(expenseModel, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Object> postExpense(@Valid @RequestBody ExpenseModel expenseModel) {
        try {
            ExpenseModel expense = expenseService.addExpense(expenseModel);
            return new ResponseEntity<>(expense, HttpStatus.CREATED); // ALTERADO: retorna o objeto salvo
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ALTERADO: "expenses/{id}" virou "/{id}"
    @PutMapping("/{id}")
    public ResponseEntity<Object> putExpense(@PathVariable("id") Long id, @Valid @RequestBody ExpenseModel expense) {
        try {
            ExpenseModel expenseModel = expenseService.updateExpense(id, expense);
            return new ResponseEntity<>(expenseModel, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // ALTERADO: "/expense/{id}" virou "/{id}", e foi adicionado o @PathVariable
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteExpense(@PathVariable("id") Long id) {
        try {
            expenseService.deleteExpenseById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}