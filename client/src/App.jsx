import Home from "./components/home/Home.jsx";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { useEffect, useState } from "react";
import Header from "./components/header/Header.jsx";
import LoginForm from "./components/login/Login.jsx";
import RegisterForm from "./components/register/Register.jsx";
import { AuthContext } from "./contexts/AuthContext.js";
import useSession from "./hooks/useAuth.js";
import Transactions from "./components/transactions/Transaction.jsx";
import PrivateRoute from "./components/routing/PrivateRoute.jsx";

function App() {
    const [authState, setAuthState] = useState({});

    const changeAuthState = (state) => {
        setAuthState(state);
    };

    useSession( changeAuthState );

    const contextData = {
        id: authState.id,
        username: authState.username,
        isAuthenticated: !!authState.username,
        changeAuthState,
    };

    return (
        <AuthContext.Provider value={contextData}>
            <BrowserRouter>
                <Header />
                <Routes>
                    <Route path="/" element={
                        <PrivateRoute>
                            <Home />
                        </PrivateRoute>
                    } />
                    <Route path="/transactions" element={
                        <PrivateRoute>
                            <Transactions />
                        </PrivateRoute>
                    } />
                    <Route path="/login" element={<LoginForm />} />
                    <Route path="/register" element={<RegisterForm />} />
                </Routes>
            </BrowserRouter>
        </AuthContext.Provider>
    );
}

export default App;
