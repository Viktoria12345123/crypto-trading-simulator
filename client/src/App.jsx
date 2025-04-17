import Home from "./components/home/Home.jsx";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { useEffect, useState } from "react";
import Header from "./components/header/Header.jsx";
import LoginForm from "./components/login/Login.jsx";
import RegisterForm from "./components/register/Register.jsx";
import { AuthContext } from "./contexts/AuthContext.js";

function App() {
    const [authState, setAuthState] = useState({});

    const changeAuthState = (state) => {
        setAuthState(state);
    };

    const contextData = {
        id: authState.id,
        username: authState.username,
        isAuthenticated: !!authState.username,
        changeAuthState,
    };

    // useEffect(() => {
    //     const initializeAuthState = async () => {
    //         const response = await getSession();
    //
    //         if (response) {
    //             changeAuthState({
    //                 _id: response.id,
    //                 email: response.email,
    //                 isAdmin: response.role == 'ADMIN'
    //             });
    //         }
    //
    //     }
    //
    //     initializeAuthState();
    // }, []);

    return (
        <AuthContext.Provider value={contextData}>
            <BrowserRouter>
                <Header />
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/login" element={<LoginForm />} />
                    <Route path="/register" element={<RegisterForm />} />
                </Routes>
            </BrowserRouter>
        </AuthContext.Provider>
    );
}

export default App;
