const BASE_URL = '/api/expenses';

export class ApiError extends Error {
    constructor(message, status, errors = {}) {
        super(message);
        this.name = 'ApiError';
        this.status = status;
        this.errors = errors;
    }
}

async function request(path = '', options = {}) {
    const config = {
        method: options.method ?? 'GET',
        headers: { Accept: 'application/json' },
    };

    if (options.body !== undefined) {
        config.headers['Content-Type'] = 'application/json';
        config.body = JSON.stringify(options.body);
    }

    let response;
    try {
        response = await fetch(BASE_URL + encodePath(path), config);
    } catch {
        throw new ApiError('Não foi possível conectar ao servidor.', 0);
    }

    if (!response.ok) {
        const problem = await response.json().catch(() => ({}));
        throw new ApiError(
            problem.detail ?? `Erro ${response.status}`,
            response.status,
            problem.errors,
        );
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

function encodePath(path) {
    return path
    .split('/')
    .map(encodeURIComponent)
    .join('/');
}

export function listExpenses() {
  return request();
}

export function getExpense(id) {
  return request(`/${id}`);
}

export function getTotal() {
  return request('/total');
}

export function createExpense(data) {
  return request('', { method: 'POST', body: data });
}

export function updateExpense(id, data) {
  return request(`/${id}`, { method: 'PUT', body: data });
}

export function deleteExpense(id) {
  return request(`/${id}`, { method: 'DELETE' });
}