import React from "react";
export default function ErrorMessage({ error }) { return error ? <div className="alert error" role="alert">{error.message || error}</div> : null; }
