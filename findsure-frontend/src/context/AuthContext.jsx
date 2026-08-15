import React from "react";
import { createContext, useContext, useState } from 'react';
import { clearSession, getSession, setSession } from '../services/api';
import * as auth from '../services/authService';
const AuthContext = createContext(null);
export function AuthProvider({ children }) {
  const [session, setCurrentSession] = useState(getSession());
  const signIn = async (email, password) => { const result = await auth.login(email, password); const next = { token: result.token, user: result.user, expiresIn: result.expiresIn }; setSession(next); setCurrentSession(next); return result; };
  const signOut = () => { clearSession(); setCurrentSession(null); };
  return <AuthContext.Provider value={{ user: session?.user, isAuthenticated: Boolean(session?.token), signIn, signOut }}>{children}</AuthContext.Provider>;
}
export const useAuth = () => useContext(AuthContext);
