import { request } from './api';
export const listItems = ({ page = 0, size = 20, search, status } = {}) => { const p = new URLSearchParams({ page, size }); if (search) p.set('search', search); if (status) p.set('status', status); return request(`/api/items?${p}`); };
export const getItem = (id) => request(`/api/items/${id}`);
export const createItem = (data) => request('/api/items', { method: 'POST', body: JSON.stringify(data) });
export const updateItem = (id, data) => request(`/api/items/${id}`, { method: 'PUT', body: JSON.stringify(data) });
export const deleteItem = (id) => request(`/api/items/${id}`, { method: 'DELETE' });
export const setItemStatus = (id, status) => request(`/api/items/${id}/${status}`, { method: 'POST' });
export const getItemScans = (id) => request(`/api/items/${id}/scans`);
export const getItemContacts = (id) => request(`/api/items/${id}/contacts`);
