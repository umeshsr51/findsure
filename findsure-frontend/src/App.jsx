import React from "react";
import { Route, Routes, useLocation } from 'react-router-dom'; import Header from './components/Header'; import Footer from './components/Footer'; import ProtectedRoute from './components/ProtectedRoute'; import { Home, InfoPage } from './pages/PublicPages'; import { Login, Register } from './pages/AuthPages'; import { DashboardLayout, DashboardHome } from './pages/Dashboard'; import { Items, ItemDetails, ItemForm } from './pages/Items'; import { Notifications, Profile, Scans, Settings } from './pages/OwnerPages'; import ScanItem from './pages/ScanItem';
function PublicLayout({ children }) { return <><Header />{children}<Footer /></>; }
function NotFound() { return <main className="section shell prose"><h1>Page not found</h1><p>The page you requested does not exist.</p></main> }
export default function App() { return <Routes><Route path="/s/:qrToken" element={<ScanItem />} /><Route path="/" element={<PublicLayout><Home /></PublicLayout>} /><Route path="/:type" element={<PublicLayout><InfoRoute /></PublicLayout>} /><Route path="/login" element={<Login />} /><Route path="/register" element={<Register />} /><Route path="/dashboard" element={<ProtectedRoute><DashboardLayout /></ProtectedRoute>}><Route index element={<DashboardHome />} /><Route path="items" element={<Items />} /><Route path="items/add" element={<ItemForm />} /><Route path="items/:id" element={<ItemDetails />} /><Route path="items/:id/edit" element={<ItemForm edit />} /><Route path="scans" element={<Scans />} /><Route path="notifications" element={<Notifications />} /><Route path="profile" element={<Profile />} /><Route path="settings" element={<Settings />} /></Route><Route path="*" element={<PublicLayout><NotFound /></PublicLayout>} /></Routes> }
function InfoRoute() {
    const location = useLocation();
    const type = location.pathname.slice(1);

    return ['how-it-works', 'features', 'about', 'faq', 'contact'].includes(type)
        ? <InfoPage type={type} />
        : <NotFound />;
}
