const BASE_URL = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '');
const TOKEN_KEY = 'findsure.session';

export class ApiError extends Error { constructor(message, status, code) { super(message); this.status = status; this.code = code; } }
export const getSession = () => { try { return JSON.parse(sessionStorage.getItem(TOKEN_KEY)); } catch { return null; } };
export const setSession = (session) => sessionStorage.setItem(TOKEN_KEY, JSON.stringify(session));
export const clearSession = () => sessionStorage.removeItem(TOKEN_KEY);

export async function request(path, options = {}) {
  const session = getSession();
  const headers = new Headers(options.headers || {});
  if (options.body && !headers.has('Content-Type')) headers.set('Content-Type', 'application/json');
  if (session?.token) headers.set('Authorization', `Bearer ${session.token}`);
  let response;
  try { response = await fetch(`${BASE_URL}${path}`, { ...options, headers }); }
  catch { throw new ApiError('Unable to reach FindSure. Check your connection and try again.', 0); }
  if (response.status === 204) return null;
  const contentType = response.headers.get('content-type') || '';
  const body = contentType.includes('application/json') ? await response.json() : await response.blob();
  if (!response.ok) {
    const error = body?.error;
    if (response.status === 401) clearSession();
    throw new ApiError(error?.message || 'Something went wrong. Please try again.', response.status, error?.code);
  }
  return body;
}
export const apiUrl = (path) => `${BASE_URL}${path}`;
