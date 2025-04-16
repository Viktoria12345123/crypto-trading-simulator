
import Home from "./components/home/Home.jsx";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {useEffect, useState} from "react";


function App() {

    const [authState, setAuthState] = useState({});

    const changeAuthState = (state) => {
        setAuthState(state)
    }

    const contextData = {
        id: authState.id,
        username: authState.username,
        isAuthenticated: !!authState.username,
        changeAuthState
    }

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

        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home />} />
            </Routes>
        </BrowserRouter>

    );
}

export default App
