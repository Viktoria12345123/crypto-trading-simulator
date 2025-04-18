import React, { useEffect, useState } from "react";
import {getHoldings} from "../../api/crypto-api.js";

export default function CurrencyHolding() {
    const [holdings, setHoldings] = useState([]);

    useEffect(() => {
        const fetchHoldings = async () => {
            const data = await getHoldings();
            setHoldings(data);

        };
        fetchHoldings();
    }, []);


    return (
        <>
            <div className="currency-holding-list">
                <h2 className="currency-title">Currency Holdings</h2>
                <div className="currency-list">
                    {holdings.map((holding, index) => (
                        <div key={index} className="currency-item">
                            <span className="currency-name">{holding.symbol}</span>
                            <span className="currency-amount">{holding.amount} {holding.symbol}</span>
                        </div>
                    ))}
                </div>
            </div>
        </>
    );
}
