import React, { useState } from "react";
import "@fortawesome/fontawesome-free/css/all.min.css";
import Sell from "./modal/Sell.jsx";
import CurrencyItem from "./CurrencyItem.jsx";
import CurrencyHolding from "./CurrencyHolding.jsx";
import CurrencyList from "./CurrencyList.jsx";

export default function Home() {
    const [isModalOpen, setIsModalOpen] = useState(false);

    const currencies = [
        { name: "Bitcoin", code: "BTC" },
        { name: "Ethereum", code: "ETH" },
        { name: "Litecoin", code: "LTC" }
    ];


    const handleSellClick = () => {
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
    };

    const handleProceed = (amount) => {
        setCryptoAmount(amount);
        console.log("Proceeding with the sale of", amount, "crypto");
    };

    console.log('Home component is rendered');

    return (
        <div className="home-container">
            <div className="sections-container">
                <div className="balance-section">
                    <h1 className="balance">
                        <p className="current-balance-text">Current Balance</p>
                        <span className="currency-sign">$</span>
                        <span className="balance-value">10,000</span>
                        <span className="currency-code"> USD</span>
                    </h1>

                    <div className="button-container">
                        <div className="button-group">
                            <button className="transaction-btn">
                                <i className="fa-solid fa-money-bill-transfer"></i>
                            </button>
                            <p className="button-label">Transactions</p>
                        </div>
                        <div className="button-group">
                            <button className="reset-btn">
                                <i className="fa-solid fa-rotate"></i>
                            </button>
                            <p className="button-label">Reset</p>
                        </div>
                        <div className="button-group">
                            <button className="buy-btn">
                                <i className="fa-solid fa-cart-shopping"></i>
                            </button>
                            <p className="button-label">Buy</p>
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

            <CurrencyList/>

            <Sell
                isOpen={isModalOpen}
                onClose={handleCloseModal}
                onProceed={handleProceed}
                currencies={currencies}
            />
        </div>
    );
}
