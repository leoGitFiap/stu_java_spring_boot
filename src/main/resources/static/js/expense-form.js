import { createExpense, ApiError } from "./api.js";

const form = document.getElementById('expense-form');

form.addEventListener('submit', handleSubmit);

function blankToNull(value) {
    const trimmed = (value ?? '').trim();
    return trimmed === '' ? null : trimmed;
}

function buildExpensePayload(formElement) {
    const data = Object.fromEntries(new FormData(formElement));

    const payload = {
        amount: blankToNull(data.amount),
        description: blankToNull(data.description),
        category: blankToNull(data.category),
    };

    const date = blankToNull(data.date);
    if (date) {
        payload.date = date;
    }

    return payload;
}

const categoryInput = form.elements.category;

categoryInput.addEventListener('input', () => {
    const value = categoryInput.value.trim();
    if (value !== '' && value.length < 3) {
        categoryInput.setCustomValidity('A categoria precisa de ao menos 3 letras.');
    } else {
        categoryInput.setCustomValidity('');
    }
});

async function handleSubmit(event) {
    event.preventDefault();
    const formElement = event.currentTarget;

    if (!formElement.checkValidity()) {
        formElement.reportValidity();
        return;
    }

    const payload = buildExpensePayload(formElement);
    const submitButton = formElement.querySelector('button[type="submit"]');
    submitButton.disabled = true;

    try {
        const created = await createExpense(payload);
    } catch (error) {
        console.error(error);
    } finally {}
}

const feedbackElement = document.querySelector('#feedback');

function showFeedback(message, type = 'success') {
    feedbackElement.textContent = message;
    feedbackElement.className = `feedback feedback--${type}`;
}

function clearFeedback() {
    feedbackElement.textContent = '';
    feedbackElement.className = '';
}

