import { request } from './api';
export const login = (email, password) => request('/api/auth/login', { method: 'POST', body: JSON.stringify({ email, password }) });
export const register = (payload) => request('/api/auth/register', { method: 'POST', body: JSON.stringify(payload) });
