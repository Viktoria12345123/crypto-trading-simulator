import { useEffect } from "react";
import { getSession } from "../api/auth-api.js";

const useSession = (changeAuthState) => {

    useEffect(() => {
        const fetchSession = async () => {
            try {
                const response = await getSession();
                const data = response.data;

                changeAuthState({
                    id: data.id,
                    username: data.username
                });
            } catch (err) {
            }
        };
        fetchSession();
    }, []);
};

export default useSession;
