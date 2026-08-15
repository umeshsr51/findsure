import React, { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";

import * as items from "../services/itemService";
import ItemCard from "../components/ItemCard";
import Loading from "../components/Loading";
import ErrorMessage from "../components/ErrorMessage";
import QRCodeDisplay from "../components/QRCodeDisplay";


// ===============================
// ITEMS LIST
// ===============================
export function Items() {
    const [data, setData] = useState();
    const [search, setSearch] = useState("");
    const [status, setStatus] = useState("");
    const [error, setError] = useState();

    const load = async () => {
        try {
            setError(undefined);

            const result = await items.listItems({
                search,
                status
            });

            setData(result);
        } catch (err) {
            setError(err);
        }
    };

    useEffect(() => {
        load();
    }, [status]);

    const submit = (e) => {
        e.preventDefault();
        load();
    };

    const change = async (item, next) => {
        if (!confirm(`Mark ${item.name} as ${next}?`)) {
            return;
        }

        try {
            await items.setItemStatus(item.id, next);
            await load();
        } catch (err) {
            setError(err);
        }
    };

    const remove = async (item) => {
        if (
            !confirm(
                `Delete ${item.name}? This cannot be undone from the app.`
            )
        ) {
            return;
        }

        try {
            await items.deleteItem(item.id);
            await load();
        } catch (err) {
            setError(err);
        }
    };

    return (
        <>
            <div className="page-heading">
                <div>
                    <p className="eyebrow">Inventory</p>
                    <h1>My items</h1>
                </div>

                <Link
                    className="button small"
                    to="/dashboard/items/add"
                >
                    Add item
                </Link>
            </div>

            <form className="filters" onSubmit={submit}>
                <input
                    aria-label="Search items"
                    placeholder="Search by name"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />

                <select
                    value={status}
                    onChange={(e) => setStatus(e.target.value)}
                >
                    <option value="">All statuses</option>
                    <option value="active">Active</option>
                    <option value="lost">Lost</option>
                    <option value="found">Found</option>
                </select>

                <button className="button secondary small">
                    Search
                </button>
            </form>

            <ErrorMessage error={error} />

            {!data ? (
                <Loading />
            ) : data.items && data.items.length ? (
                <div className="item-list">
                    {data.items.map((item) => (
                        <ItemCard
                            key={item.id}
                            item={item}
                            onStatus={change}
                            onDelete={remove}
                        />
                    ))}
                </div>
            ) : (
                <div className="empty">
                    No items match this view.{" "}
                    <Link to="/dashboard/items/add">
                        Add your first item.
                    </Link>
                </div>
            )}
        </>
    );
}


// ===============================
// ADD / EDIT ITEM FORM
// ===============================
export function ItemForm({ edit = false }) {
    const { id } = useParams();
    const navigate = useNavigate();

    const [form, setForm] = useState({
        name: "",
        category: "",
        description: "",
        photoUrl: ""
    });

    const [error, setError] = useState();
    const [loading, setLoading] = useState(edit);

    useEffect(() => {
        const loadItem = async () => {
            if (!edit) {
                return;
            }

            try {
                const item = await items.getItem(id);

                setForm({
                    name: item.name || "",
                    category: item.category || "",
                    description: item.description || "",
                    photoUrl: item.photoUrl || ""
                });
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        loadItem();
    }, [edit, id]);

    const submit = async (e) => {
        e.preventDefault();

        setLoading(true);
        setError(undefined);

        try {
            let saved;

            if (edit) {
                saved = await items.updateItem(id, form);
            } else {
                saved = await items.createItem(form);
            }

            navigate(`/dashboard/items/${saved.id}`);
        } catch (err) {
            setError(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <p className="eyebrow">
                {edit ? "Edit item" : "New item"}
            </p>

            <h1>
                {edit ? "Update item" : "Add an item"}
            </h1>

            {loading ? (
                <Loading />
            ) : (
                <form
                    className="panel form"
                    onSubmit={submit}
                >
                    <ErrorMessage error={error} />

                    <label>
                        Name

                        <input
                            required
                            maxLength="120"
                            value={form.name}
                            onChange={(e) =>
                                setForm({
                                    ...form,
                                    name: e.target.value
                                })
                            }
                        />
                    </label>

                    <label>
                        Category

                        <input
                            maxLength="60"
                            value={form.category}
                            onChange={(e) =>
                                setForm({
                                    ...form,
                                    category: e.target.value
                                })
                            }
                        />
                    </label>

                    <label>
                        Description

                        <textarea
                            value={form.description}
                            onChange={(e) =>
                                setForm({
                                    ...form,
                                    description: e.target.value
                                })
                            }
                        />
                    </label>

                    <label>
                        Photo URL

                        <input
                            type="url"
                            maxLength="500"
                            value={form.photoUrl}
                            onChange={(e) =>
                                setForm({
                                    ...form,
                                    photoUrl: e.target.value
                                })
                            }
                        />
                    </label>

                    <button
                        className="button"
                        disabled={loading}
                    >
                        {loading ? "Saving…" : "Save item"}
                    </button>
                </form>
            )}
        </>
    );
}


// ===============================
// ITEM DETAILS
// ===============================
export function ItemDetails() {
    const { id } = useParams();

    const [item, setItem] = useState();
    const [contacts, setContacts] = useState([]);
    const [error, setError] = useState();

    useEffect(() => {
        const loadDetails = async () => {
            try {
                const itemData = await items.getItem(id);
                setItem(itemData);
            } catch (err) {
                setError(err);
            }

            try {
                const contactData =
                    await items.getItemContacts(id);

                setContacts(contactData);
            } catch (err) {
                // Finder messages are optional.
                // Do not break the item details page if they fail.
                setContacts([]);
            }
        };

        loadDetails();
    }, [id]);

    if (error) {
        return <ErrorMessage error={error} />;
    }

    if (!item) {
        return <Loading />;
    }

    return (
        <>
            <div className="page-heading">
                <div>
                    <p className="eyebrow">
                        Item details
                    </p>

                    <h1>{item.name}</h1>
                </div>

                <Link
                    className="button secondary small"
                    to={`/dashboard/items/${id}/edit`}
                >
                    Edit
                </Link>
            </div>

            <div className="detail-grid">

                {/* ITEM INFORMATION */}
                <section className="panel">
                    <span
                        className={`badge ${item.status}`}
                    >
                        {item.status}
                    </span>

                    <p>
                        {item.category ||
                            "Uncategorized"}
                    </p>

                    <p>
                        {item.description ||
                            "No description added."}
                    </p>

                    <dl>
                        <dt>Created</dt>

                        <dd>
                            {new Date(
                                item.createdAt
                            ).toLocaleString()}
                        </dd>

                        <dt>Scans</dt>

                        <dd>
                            {item.scanCount || 0}
                        </dd>

                        <dt>Last scan</dt>

                        <dd>
                            {item.lastScan
                                ? new Date(
                                    item.lastScan.scannedAt
                                ).toLocaleString()
                                : "No scans yet"}
                        </dd>
                    </dl>
                </section>


                {/* QR CODE */}
                <section className="panel">
                    <h2>Your QR code</h2>

                    <QRCodeDisplay
                        itemId={item.id}
                        token={item.qrToken}
                    />
                </section>

            </div>


            {/* FINDER MESSAGES */}
            <section className="panel">
                <h2>Finder messages</h2>

                {contacts.length ? (
                    <ul className="activity">
                        {contacts.map((contact) => (
                            <li key={contact.id}>
                                <b>
                                    {contact.name ||
                                        "Anonymous finder"}
                                </b>

                                <p>
                                    {contact.message}
                                </p>

                                {contact.email && (
                                    <small>
                                        {contact.email}
                                    </small>
                                )}
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p className="muted">
                        No finder messages yet.
                    </p>
                )}
            </section>
        </>
    );
}