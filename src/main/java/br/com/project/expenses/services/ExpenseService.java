package br.com.project.expenses.services;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.project.expenses.model.ExpenseModel;
import br.com.project.expenses.repository.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;

@Service 
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public ExpenseModel addExpense(ExpenseModel expense){
        return expenseRepository.save(expense);
    }

    public List<ExpenseModel> readAllExpenses(){
        return expenseRepository.findAll();
    }

    public ExpenseModel readExpenseById(Long id){
        return expenseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ID: " + id + " não encontrado."));
    }

    public ExpenseModel updateExpense(Long id, ExpenseModel expenseModel){
        return expenseRepository.findById(id).map(
                existingExpense -> {
                    existingExpense.setAmount(expenseModel.getAmount());
                    existingExpense.setDescription(expenseModel.getDescription());
                    existingExpense.setCategory(expenseModel.getCategory());
                    existingExpense.setDate(expenseModel.getDate());

                    return expenseRepository.save(existingExpense);
                })
                .orElseThrow(() -> new EntityNotFoundException("Valor não encontrado."));
    }

    public void deleteExpenseById(Long id){
        try {
            readExpenseById(id);
            expenseRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Pedido não encontrado no ID: " + id);
        }
    }

    
}
