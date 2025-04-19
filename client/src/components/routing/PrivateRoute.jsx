import React, { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { useAuthContext } from "../../contexts/AuthContext";

export default function PrivateRoute({ children }) {
    const { username, isAuthenticated } = useAuthContext();
    const [loading, setLoading] = useState(true);

    useEffect(() => {

        if (username !== undefined) {
            setLoading(false);
        }
    }, [username]);

    if (loading) {
        return null;
    }

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    return children;
}
