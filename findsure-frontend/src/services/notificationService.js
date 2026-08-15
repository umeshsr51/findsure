import { request } from './api';
export const getNotifications = () => request('/api/notifications');
export const markNotificationRead = (id) => request(`/api/notifications/${id}/read`, { method: 'PUT' });
