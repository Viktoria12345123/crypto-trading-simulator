import React from "react";
import { Link } from "react-router-dom";
import {useAuthContext} from "../../contexts/AuthContext.js";

export default function Header() {

    const { username } = useAuthContext();

    return (
        <header className="header">
            <h1 className="logo">Crypto Trade</h1>
            <div className="auth-buttons">

                {!username ? (
                    <>
                        <Link to="/login">
                            <button className="auth-button square-button">Login</button>
                        </Link>
                        <Link to="/register">
                            <button className="auth-button square-button">Register</button>
                        </Link>
                    </>
                ) : (
                    <span>Welcome, {username}!</span>
                )}
            </div>
        </header>
    );
}
