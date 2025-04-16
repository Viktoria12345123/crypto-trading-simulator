import React from "react";

export default function CurrencyHolding() {
    return (
        <div className="currency-holding-list">
            <h2 className="currency-title">Currency Holdings</h2>
            <div className="currency-list">
                {/* Currency Item 1 */}
                <div className="currency-item">
                    <span className="currency-name">Bitcoin</span>
                    <span className="currency-amount">2.5 BTC</span>
                    <span className="currency-value">$55,000</span>
                </div>
                {/* Currency Item 2 */}
                <div className="currency-item">
                    <span className="currency-name">Ethereum</span>
                    <span className="currency-amount">10 ETH</span>
                    <span className="currency-value">$20,000</span>
                </div>
                <div className="currency-item">
                    <span className="currency-name">Bitcoin</span>
                    <span className="currency-amount">2.5 BTC</span>
                    <span className="currency-value">$55,000</span>
                </div>
                <div className="currency-item">
                    <span className="currency-name">Bitcoin</span>
                    <span className="currency-amount">2.5 BTC</span>
                    <span className="currency-value">$55,000</span>
                </div>
                {/* Currency Item 3 */}
                <div className="currency-item">
                    <span className="currency-name">Litecoin</span>
                    <span className="currency-amount">50 LTC</span>
                    <span className="currency-value">$6,500</span>
                </div>
            </div>
        </div>
    );
}
