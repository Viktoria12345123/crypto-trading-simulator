import { useEffect, useState } from "react";
import { getBalance } from "../api/user-api.js";

const useBalance = () => {
    const [balance, setBalance] = useState(0.00);

    useEffect(() => {
        const fetchBalance = async () => {
            try {
                const response = await getBalance();
                setBalance(response.data.balance);
            } catch (error) {
                console.error("Failed to fetch balance:", error);
            }
        };

        fetchBalance();
    }, []);

    return balance;
};

export default useBalance;
