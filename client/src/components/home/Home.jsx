import React, { useRef, useState, useEffect } from "react";
import axios from "axios";
import "@fortawesome/fontawesome-free/css/all.min.css";
import Sell from "./modal/Sell.jsx";
import CurrencyHolding from "./CurrencyHolding.jsx";
import CurrencyList from "./cryptoDisplay/CurrencyList.jsx";
import {useAuthContext} from "../../contexts/AuthContext.js";
import useBalance from "../../hooks/useBalance.js";
import useTop20Cryptos from "../../hooks/useTop20Cryptos.js";

export default function Home() {
    const [isSellModalOpen, setIsSellModalOpen] = useState(false);
    const pricesRef = useRef({});

    const { id } = useAuthContext();
    const balance = useBalance();
    const top20Cryptocurrencies = useTop20Cryptos(pricesRef);

    const formattedBalance = new Intl.NumberFormat("en-US", {
        currency: "USD"
    }).format(balance);

    console.log("reload")

    const handleSellClick = () => setIsSellModalOpen(true);
    const handleCloseSellModal = () => setIsSellModalOpen(false);
    const handleProceed = (amount) => console.log("Proceeding with the sale of", amount, "crypto");

    return (
        <div className="home-container">
            <div className="sections-container">
                <div className="balance-section">
                    <h1 className="balance">
                        <p className="current-balance-text">Current Balance</p>
                        <span className="currency-sign">$</span>
                        <span className="balance-value">{formattedBalance}</span>
                        <span className="currency-code"> USD</span>
                    </h1>
                    <div className="button-container">
                        <div className="button-group">
                            <button className="transaction-btn"><i className="fa-solid fa-money-bill-transfer"></i></button>
                            <p className="button-label">Transactions</p>
                        </div>
                        <div className="button-group">
                            <button className="reset-btn"><i className="fa-solid fa-rotate"></i></button>
                            <p className="button-label">Reset</p>
                        </div>
                        <div className="button-group">
                            <button className="sell-btn" onClick={handleSellClick}>
                                <i className="fa-solid fa-money-bills"></i>
                            </button>
                            <p className="button-label">Sell</p>
                        </div>
                    </div>
                </div>

                <div className="currency-holding">
                    <CurrencyHolding/>
                </div>
            </div>

            <CurrencyList
                top20Cryptocurrencies={top20Cryptocurrencies}
                pricesRef={pricesRef}
                balance={balance}
            />

            <Sell
                isOpen={isSellModalOpen}
                onClose={handleCloseSellModal}
                onProceed={handleProceed}
                currencies={top20Cryptocurrencies}
            />
        </div>
    );
}
