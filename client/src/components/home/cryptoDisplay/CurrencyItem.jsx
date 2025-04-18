import React, { useState } from "react";
import {buy} from "../../../api/crypto-api.js";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export default function CurrencyItem({ symbol, displayName, price, currency , balance}) {
    const [isBuying, setIsBuying] = useState(false);
    const [buyAmount, setBuyAmount] = useState("");

    symbol = symbol.toUpperCase()

    const formattedPrice = (price) => {
        const number = parseFloat(price);
        if (isNaN(number)) return "Loading...";

        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: currency,
            minimumFractionDigits: 2,
            maximumFractionDigits: 4,
        }).format(number);
    };

    const handleBuyClick = async () => {
        if (!isBuying) {
            setIsBuying(true);
        } else {
            const cost = parseFloat(price) * buyAmount

            if(cost > balance) {
                toast.error(`Insufficient funds for buying ${symbol} - ${cost}$`);
                return
            }

            await buy(
                buyAmount,
                cost,
                symbol,
                displayName,
            );

            setIsBuying(false);
            setBuyAmount("");
            toast.success(`${buyAmount} ${symbol} bought successfully!`);


        }
    };

    return (
        <>
            <ToastContainer />
            <div className="crypto-item">
                <div className="crypto-icon-wrapper">
                    <i className="fa-solid fa-bitcoin-sign"></i>
                </div>
                <div className="crypto-info">
                    <h3>
                        {displayName} ({symbol})
                        <button className="buy-button" onClick={handleBuyClick}>
                            {isBuying ? "Confirm" : "Buy"}
                        </button>
                    </h3>
                    {isBuying && (
                        <div className="buy-inline-input">
                            <input
                                type="number"
                                value={buyAmount}
                                onChange={(e) => setBuyAmount(e.target.value)}
                                placeholder="Amount"
                                className="buy-input"
                                step="0.01"
                            />
                        </div>
                    )}
                </div>
                <p className="crypto-price">{formattedPrice(price)} {currency}</p>
            </div>
        </>
    );

}
