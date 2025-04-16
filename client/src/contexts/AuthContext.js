import { createContext, useContext } from "react";

export const AuthContext = createContext ({
    id: '',
    username: '',
    isAuthenticated: false,
    changeAuthState: ( ) => null
})

export const useAuthContext = () => {
    return useContext(AuthContext)
}