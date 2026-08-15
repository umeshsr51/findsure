import React, { useState } from 'react';
import { Link, NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const publicLinks = [
  ['/how-it-works', 'How it works'],
  ['/features', 'Features'],
  ['/about', 'About'],
  ['/faq', 'FAQ'],
  ['/contact', 'Contact'],
];

export default function Header() {
  const [open, setOpen] = useState(false);
  const { isAuthenticated, signOut } = useAuth();
  const close = () => setOpen(false);

  return (
    <header className="header">
      <div className="shell nav">
        <Link className="brand" to="/" onClick={close}>
          <span className="brand-mark" aria-hidden="true">✓</span>
          <span>Find<span>Sure</span></span>
        </Link>
        <button
          className="menu-button"
          aria-label="Toggle navigation"
          aria-expanded={open}
          onClick={() => setOpen((current) => !current)}
        >
          <span className="menu-icon" aria-hidden="true" />
        </button>
        <nav className={open ? 'navlinks open' : 'navlinks'} aria-label="Primary navigation">
          <div className="navlinks-primary">
            {publicLinks.map(([to, label]) => (
              <NavLink key={to} to={to} onClick={close}>
                {label}
              </NavLink>
            ))}
          </div>
          <div className="navlinks-actions">
            {isAuthenticated ? (
              <>
                <NavLink className="dashboard-link" to="/dashboard" onClick={close}>Dashboard</NavLink>
                <button className="link-button sign-out" onClick={() => { signOut(); close(); }}>Sign out</button>
              </>
            ) : (
              <NavLink className="login-link" to="/login" onClick={close}>Log in</NavLink>
            )}
          </div>
        </nav>
      </div>
    </header>
  );
}
