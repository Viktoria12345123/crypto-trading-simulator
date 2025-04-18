import React, { useState } from "react";

function Buy({ isOpen, onClose, currencies }) {
    const [amount, setAmount] = useState("");
    const [selectedCurrency, setSelectedCurrency] = useState(currencies[0].code);

    const handleAmountChange = (e) => {
        setAmount(e.target.value);
    };

    const handleProceed = ( selectedCurrency ,amount) => {
        console.log("Proceeding with the sale of", amount, "crypto");
    };

    const handleCurrencyChange = (e) => {
        setSelectedCurrency(e.target.value);
    };

    return (
        isOpen && (
            <div className="modal-overlay">
                <div className="modal-container">
                    <div className="modal-header">
                        <span>Specify Amount To BUY</span>
                    </div>
                    <div className="modal-body">
                        <div className="input-section">
                            <label htmlFor="currency-select">Choose Currency:</label>
                            <select
                                id="currency-select"
                                value={selectedCurrency}
                                onChange={handleCurrencyChange}
                                className="input-field"
                            >
                                {currencies.map((currency) => (
                                    <option key={currency.symbol} value={currency.symbol}>
                                        {currency.name} ({currency.symbol})
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div className="input-section">
                            <input
                                type="number"
                                value={amount}
                                onChange={handleAmountChange}
                                placeholder="Enter amount"
                                className="input-field"
                            />
                        </div>
                    </div>
                    <div className="modal-footer">
                        <button className="cancel-btn" onClick={onClose}>
                            Cancel
                        </button>
                        <button
                            className="proceed-btn"
                            onClick={() => {
                                handleProceed(selectedCurrency, amount);
                                onClose();
                            }}
                        >
                            Proceed
                        </button>
                    </div>
                </div>
            </div>
        )
    );
}

export default Buy;
