import React, { useEffect, useState } from "react";
import {Navigate, useNavigate} from "react-router-dom";
import { useAuthContext } from "../../contexts/AuthContext";

export default function PrivateRoute({ children }) {
    const { username, isAuthenticated } = useAuthContext();
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {

        if (username !== undefined) {
            setLoading(false);
        }
    }, [username]);

    if (loading) {
        return null;
    }

    if (!isAuthenticated) {
      navigate("/login")
    }

    return children;
}
