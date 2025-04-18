import { useEffect } from "react";
import { getSession } from "../api/auth-api.js";

const useSession = (changeAuthState) => {

    useEffect(() => {
        const fetchSession = async () => {
                const data = await getSession();

                    changeAuthState({
                        id: data.id,
                        username: data.username
                    });

        };

        fetchSession();
    }, []);
};

export default useSession;
