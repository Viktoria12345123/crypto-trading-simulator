import { useEffect, useState } from "react";
import axios from "axios";

const useTop20Cryptos = (pricesRef) => {
    const [cryptos, setCryptos] = useState([]);

    useEffect(() => {
        const fetchTop20Cryptocurrencies = async () => {
            try {
                const response = await axios.get(
                    "https://api.coingecko.com/api/v3/coins/markets",
                    {
                        params: {
                            vs_currency: "usd",
                            order: "market_cap_desc",
                            per_page: 20,
                            page: 1
                        },
                        headers: {
                            'Accept': 'application/json'
                        }
                    }
                );

                const top20 = response.data.map(coin => {
                    const pair = `${coin.symbol.toUpperCase()}/USD`;
                    pricesRef.current[pair] = coin.current_price.toString();

                    return {
                        name: coin.name,
                        symbol: coin.symbol
                    };

                });

                setCryptos(top20);
            } catch (error) {
                console.error("Error fetching cryptocurrencies:", error);
            }
        };

        fetchTop20Cryptocurrencies();
    }, []);

    return cryptos;
};

export default useTop20Cryptos;
