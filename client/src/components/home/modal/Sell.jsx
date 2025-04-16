import React, { useState } from "react";

function Sell({ isOpen, onClose, onProceed }) {
    const [amount, setAmount] = useState("");

    const handleAmountChange = (e) => {
        setAmount(e.target.value);
    };

    return (
        isOpen && (
            <div className="modal-overlay">
                <div className="modal-container">
                    <div className="modal-header">
                        <span>Specify Amount To Sell
                        </span>
                        <div className="close-btn" onClick={onClose}>
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                className="h-6 w-6 text-[#64748B]"
                                fill="none"
                                viewBox="0 0 24 24"
                                stroke="currentColor"
                                stroke-width="2"
                            >
                                <path
                                    stroke-linecap="round"
                                    stroke-linejoin="round"
                                    d="M6 18L18 6M6 6l12 12"
                                />
                            </svg>
                        </div>
                    </div>
                    <div className="modal-body">
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
                                onProceed(amount);
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

export default Sell;
