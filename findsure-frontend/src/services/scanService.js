import { request } from './api';
export const getPublicItem = (token) => request(`/api/public/items/${encodeURIComponent(token)}`);
export const recordScan = (token) => request(`/api/scan/${encodeURIComponent(token)}`, { method: 'POST' });
export const shareLocation = (scanId, data) => request(`/api/scan/${scanId}/location`, { method: 'POST', body: JSON.stringify(data) });
export const sendFinderContact = (scanId, data) => request(`/api/scan/${scanId}/contact`, { method: 'POST', body: JSON.stringify(data) });
