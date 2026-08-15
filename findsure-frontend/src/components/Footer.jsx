import React from "react";
import { Link } from 'react-router-dom';
export default function Footer() { return <footer><div className="shell footer"><div><p className="brand">Find<span>Sure</span></p><p>Find what matters. Return what matters.</p></div><nav><Link to="/contact">Contact</Link><Link to="/faq">FAQ</Link><Link to="/privacy">Privacy</Link></nav><small>© {new Date().getFullYear()} FindSure</small></div></footer>; }
