import React from "react";
import CurrencyItem from "./CurrencyItem.jsx";
import {useWebSocketPrices} from "../../../hooks/useWebSocketPrices.js";

export default function CurrencyList({ top20Cryptocurrencies , pricesRef, balance}) {

     useWebSocketPrices(top20Cryptocurrencies, pricesRef );

    return (
        <div className="crypto-section">
            {top20Cryptocurrencies.map((crypto, index) => {
                const price = pricesRef.current[`${crypto.symbol.toUpperCase()}/USD`] || 'Loading...';
                return (
                    <CurrencyItem
                        key={index}
                        symbol={crypto.symbol.toUpperCase()}
                        displayName={crypto.name}
                        price={price}
                        currency="USD"
                        balance={balance}
                    />
                );
            })}
        </div>
    );
}
