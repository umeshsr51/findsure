import React from "react";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import * as itemApi from "../services/itemService";
import {
    getNotifications,
    markNotificationRead
} from "../services/notificationService";
import { useAuth } from "../context/AuthContext";
import Loading from "../components/Loading";
import ErrorMessage from "../components/ErrorMessage";


export function Scans() {
    const [items, setItems] = useState();
    const [rows, setRows] = useState([]);
    const [error, setError] = useState();

    useEffect(() => {
        itemApi
            .listItems({ size: 100 })
            .then(async (d) => {
                setItems(d.items);

                const all = await Promise.all(
                    d.items.map(async (item) =>
                        (await itemApi.getItemScans(item.id)).map((scan) => ({
                            ...scan,
                            itemName: item.name,
                            itemId: item.id
                        }))
                    )
                );

                setRows(
                    all
                        .flat()
                        .sort(
                            (a, b) =>
                                new Date(b.scannedAt) -
                                new Date(a.scannedAt)
                        )
                );
            })
            .catch(setError);
    }, []);

    if (error) return <ErrorMessage error={error} />;
    if (!items) return <Loading />;

    return (
        <>
            <p className="eyebrow">Activity</p>
            <h1>Scan history</h1>

            {rows.length ? (
                <div className="panel table-wrap">
                    <table>
                        <thead>
                            <tr>
                                <th>Item</th>
                                <th>When</th>
                                <th>Location</th>
                                <th>Status</th>
                            </tr>
                        </thead>

                        <tbody>
                            {rows.map((row) => (
                                <tr key={row.id}>
                                    <td>
                                        <Link
                                            to={`/dashboard/items/${row.itemId}`}
                                        >
                                            {row.itemName}
                                        </Link>
                                    </td>

                                    <td>
                                        {new Date(
                                            row.scannedAt
                                        ).toLocaleString()}
                                    </td>

                                    <td>
                                        {row.locationShared
                                            ? row.approxCity ||
                                            `${row.latitude?.toFixed(
                                                4
                                            )}, ${row.longitude?.toFixed(4)}`
                                            : "Not shared"}
                                    </td>

                                    <td>
                                        {row.locationShared
                                            ? "Shared"
                                            : "Scan recorded"}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                <div className="empty">
                    No scans have been recorded yet.
                </div>
            )}
        </>
    );
}


export function Notifications() {
    const [notes, setNotes] = useState();
    const [error, setError] = useState();

    useEffect(() => {
        async function loadNotifications() {
            try {
                const data = await getNotifications();
                setNotes(data);
            } catch (e) {
                setError(e);
            }
        }

        loadNotifications();
    }, []);

    const read = async (n) => {
        if (n.read) return;

        try {
            const updated = await markNotificationRead(n.id);

            setNotes((currentNotes) =>
                currentNotes.map((x) =>
                    x.id === n.id ? updated : x
                )
            );
        } catch (e) {
            setError(e);
        }
    };

    if (error) return <ErrorMessage error={error} />;
    if (!notes) return <Loading />;

    return (
        <>
            <p className="eyebrow">Updates</p>
            <h1>Notifications</h1>

            <div className="panel">
                {notes.length ? (
                    notes.map((n) => (
                        <button
                            className={`notification ${n.read ? "" : "unread"
                                }`}
                            key={n.id}
                            onClick={() => read(n)}
                        >
                            <span>
                                <b>{n.message}</b>

                                <small>
                                    {new Date(
                                        n.createdAt
                                    ).toLocaleString()}
                                </small>
                            </span>

                            {!n.read && <em>Mark read</em>}
                        </button>
                    ))
                ) : (
                    <p className="muted">
                        You’re all caught up.
                    </p>
                )}
            </div>
        </>
    );
}


export function Profile() {
    const { user } = useAuth();

    return (
        <>
            <p className="eyebrow">Account</p>
            <h1>Profile</h1>

            <section className="panel profile">
                <dl>
                    <dt>Name</dt>
                    <dd>{user?.name}</dd>

                    <dt>Email</dt>
                    <dd>{user?.email}</dd>

                    <dt>Phone</dt>
                    <dd>{user?.phone || "Not provided"}</dd>

                    <dt>Member since</dt>
                    <dd>
                        {user?.createdAt
                            ? new Date(
                                user.createdAt
                            ).toLocaleDateString()
                            : "—"}
                    </dd>
                </dl>

                <p className="muted">
                    Profile editing is not yet supported by the backend.
                </p>
            </section>
        </>
    );
}


export function Settings() {
    const { signOut } = useAuth();

    return (
        <>
            <p className="eyebrow">Preferences</p>
            <h1>Settings</h1>

            <section className="panel">
                <h2>Session</h2>

                <p>
                    Sign out securely on this device.
                </p>

                <button
                    className="button secondary"
                    onClick={signOut}
                >
                    Log out
                </button>
            </section>
        </>
    );
}