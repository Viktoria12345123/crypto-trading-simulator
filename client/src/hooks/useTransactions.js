import { useEffect, useState } from "react";
import {getTransactions} from "../api/crypto-api.js";


export function useTransactions() {
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        const fetchTransactions = async () => {
            try {
                const response = await getTransactions();
                setTransactions(response.data);
            } catch (err) {
                console.error("Failed to fetch transactions:", err);
            }
        };

        fetchTransactions();
    }, []);

    return transactions ;
}
