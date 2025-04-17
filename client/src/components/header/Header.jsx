import React from "react";
import { Link } from "react-router-dom";


export default function Header() {
    return (
        <header className="header">
            <h1 className="logo">Crypto Trade</h1>
            <div className="auth-buttons">
                <Link to="/login">
                    <button className="auth-button square-button">Login</button>
                </Link>
                <Link to="/register">
                    <button className="auth-button square-button">Register</button>
                </Link>
            </div>
        </header>
    );
}
