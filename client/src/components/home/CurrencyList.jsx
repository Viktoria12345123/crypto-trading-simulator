import React, { useEffect, useState } from "react";
import CurrencyItem from "./CurrencyItem.jsx";

const top20Cryptocurrencies = [

    { symbol: 'DOT', name: 'Polkadot' },
    { symbol: 'ETH', name: 'Ethereum' },
    { symbol: 'LINK', name: 'Chainlink' },
    { symbol: 'USDT', name: 'Tether' },
    { symbol: 'SOL', name: 'Solana' },
    { symbol: 'USDC', name: 'USD Coin' },
    { symbol: 'ADA', name: 'Cardano' },
    { symbol: 'PAXG', name: 'Paxos Gold' },
    { symbol: 'CRV', name: 'Curve DAO Token' },
    { symbol: 'XCN', name: 'Chain' },
    { symbol: 'LTC', name: 'Litecoin' },
    { symbol: 'AVAX', name: 'Avalanche' },
    { symbol: 'ATOM', name: 'Cosmos' },
    { symbol: 'XLM', name: 'Stellar' },
    { symbol: 'TRX', name: 'Tron' },
    { symbol: 'MATIC', name: 'Polygon' },
    { symbol: 'UNI', name: 'Uniswap' },
    { symbol: 'ALGO', name: 'Algorand' },
    { symbol: 'BTC', name: 'Bitcoin' },
    { symbol: 'AAVE', name: 'Aave' }
];

export default function CurrencyList() {
    const [prices, setPrices] = useState({});
    const [ws, setWs] = useState(null);

    useEffect(() => {
        const websocket = new WebSocket('wss://ws.kraken.com');

        websocket.onopen = () => {

            const pairs = top20Cryptocurrencies.map(crypto => `${crypto.symbol}/USD`);
            console.log('Subscribing to channel...');
            websocket.send(JSON.stringify({
                event: 'subscribe',
                pair: pairs,
                subscription: { name: 'book', depth: 10 }
            }));
        };

        websocket.onmessage = (event) => {
            const message = JSON.parse(event.data);

            if (Array.isArray(message) && message[1] && message[1].a) {
                const pair = message[3];
                const askPrice = message[1].a[0][0];

                setPrices(prevPrices => ({
                    ...prevPrices,
                    [pair]: askPrice
                }));
            }
        };

        websocket.onerror = (error) => {
            console.error('WebSocket error:', error);
        };

        websocket.onclose = () => {
            console.log('WebSocket connection closed');
        };

        setWs(websocket);

        return () => {
            if (websocket) {
                websocket.close();
            }
        };
    }, []);

    return (
        <div className="crypto-section">
            {top20Cryptocurrencies.map((crypto, index) => (
                <CurrencyItem
                    key={index}
                    symbol={crypto.symbol}
                    displayName={crypto.name}
                    price={prices[`${crypto.symbol}/USD`] || 'Loading...'}
                    currency="USD"
                />
            ))}
        </div>
    );
}
