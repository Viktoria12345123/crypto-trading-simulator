import React, { useState } from "react";
import SellModal from "./SellModal";
import CryptoValue from "./CryptoValue.jsx";
import "@fortawesome/fontawesome-free/css/all.min.css";

export default function Home() {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [cryptoAmount, setCryptoAmount] = useState("");

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

    return (
        <>
            <div className="home-container">
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

                <div className="crypto-section">
                    <CryptoValue />
                </div>
            </div>

            <SellModal
                isOpen={isModalOpen}
                onClose={handleCloseModal}
                onProceed={handleProceed}
            />
        </>
    );
}
