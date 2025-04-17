import { createContext, useContext } from "react";

export const AuthContext = createContext ({
    id: null,
    username: '',
    isAuthenticated: false,
    changeAuthState: ( ) => null
})

export const useAuthContext = () => {
    return useContext(AuthContext)
}