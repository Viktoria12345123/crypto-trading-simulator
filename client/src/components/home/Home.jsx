import React, { useRef, useState } from "react";
import "@fortawesome/fontawesome-free/css/all.min.css";
import CurrencyHolding from "./CurrencyHolding.jsx";
import CurrencyList from "./cryptoDisplay/CurrencyList.jsx";
import {useAuthContext} from "../../contexts/AuthContext.js";
import useBalance from "../../hooks/useBalance.js";
import useTop20Cryptos from "../../hooks/useTop20Cryptos.js";
import {reset} from "../../api/user-api.js";
import { useNavigate } from "react-router-dom";


export default function Home() {
    const [isSellModalOpen, setIsSellModalOpen] = useState(false);
    const pricesRef = useRef({});
    const navigate = useNavigate();


    const { id } = useAuthContext();
    const balance = useBalance();
    const top20Cryptocurrencies = useTop20Cryptos(pricesRef);

    const formattedBalance = new Intl.NumberFormat("en-US", {
        currency: "USD"
    }).format(balance);

    const handleSellClick = () => setIsSellModalOpen(true);

    const handleResetClick = async() => {
        await reset();
        window.location.reload();
    };

    const handleTransactionsClick = () => {
        navigate("/transactions");
    };

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
                            <button className="transaction-btn" onClick={handleTransactionsClick}><i className="fa-solid fa-money-bill-transfer"></i></button>
                            <p className="button-label">Transactions</p>
                        </div>
                        <div className="button-group">
                            <button className="reset-btn" onClick={handleResetClick}><i className="fa-solid fa-rotate"></i></button>
                            <p className="button-label">Reset</p>
                        </div>
                    </div>
                </div>

                <div className="currency-holding">
                    <CurrencyHolding pricesRef={pricesRef} />
                </div>
            </div>

            <CurrencyList
                top20Cryptocurrencies={top20Cryptocurrencies}
                pricesRef={pricesRef}
                balance={balance}
            />
        </div>
    );
}
