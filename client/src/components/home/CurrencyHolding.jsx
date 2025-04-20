import React, { useEffect, useState } from "react";
import { getHoldings, sell } from "../../api/crypto-api.js";

export default function CurrencyHolding({pricesRef}) {
    const [holdings, setHoldings] = useState([]);

    useEffect(() => {
        const fetchHoldings = async () => {
            const response = await getHoldings();
            setHoldings(response.data);
        };
        fetchHoldings();
    }, []);

    const handleSell = async (holding) => {
        const input = prompt(`Enter amount of ${holding.symbol.toUpperCase()} to sell:`);

        if (!input || isNaN(input)) return alert("Invalid amount");

        const amount = parseFloat(input);
        if (amount <= 0 || amount > holding.amount) {
            return alert("Invalid amount. Must be between 0 and your total holdings.");
        }

        const priceKey = `${holding.symbol.toUpperCase()}/USD`;
        const livePrice = pricesRef.current[priceKey];

        if (!livePrice) {
            return alert("Live price not available.");
        }

        const cost = parseFloat((amount * livePrice).toFixed(2));

        try {
            await sell(amount, cost, holding.symbol, holding.name);
            alert(`Successfully sold ${amount} ${holding.symbol} for $${cost}`);
            window.location.reload();
        } catch (e) {
            console.error(e);
            alert("Sell failed");
        }
    };

    return (
        <div className="currency-holding-list">
            <h2 className="currency-title">Currency Holdings</h2>
            <div className="currency-list">
                {holdings.length === 0 ? (
                    <div className="no-holdings-message">No holdings yet.</div>
                ) : (
                    holdings.map((holding, index) => (
                        <div key={index} className="currency-item">
                            <span className="currency-name">{holding.symbol.toUpperCase()}</span>
                            <span className="currency-amount">
                                {holding.amount} {holding.symbol.toUpperCase()}
                            </span>
                            <button className="sell-button" onClick={() => handleSell(holding)}>
                                Sell
                            </button>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}
