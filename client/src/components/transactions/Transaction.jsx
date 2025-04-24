import React from "react";
import { useNavigate } from "react-router-dom";
import {useTransactions} from "../../hooks/useTransactions.js";

export default function Transactions() {
    const navigate = useNavigate();
    const transactions= useTransactions();

    const handleNavigateHome = () => {
        navigate("/home");
    };

    return (
        <div className="transactions-container">
            <div className="transactions-header">
                <div>
                    <h3 className="transactions-title">Transaction History</h3>
                    <p className="transactions-subtitle">Your recent buys and sells</p>
                </div>
            </div>

            {transactions.length === 0 ? (
                <p className="transactions-empty">No transactions found.</p>
            ) : (
                <table className="transactions-table">
                    <thead>
                    <tr>
                        <th>Type</th>
                        <th>Symbol</th>
                        <th>Amount</th>
                        <th>Price/Unit ($)</th>
                        <th>Cost ($)</th>
                        <th>Profit ($)</th>
                    </tr>
                    </thead>
                    <tbody>
                    {transactions.map((tx, idx) => (
                        <tr key={idx}>
                            <td>{tx.type}</td>
                            <td>{tx.coinSymbol}</td>
                            <td>{tx.amount}</td>
                            <td>{tx.pricePerUnit}</td>
                            <td>
                                {tx.type === "BUY" ? Math.abs(tx.profit) : "-"}
                            </td>
                            <td className={tx.type === "SELL" && parseFloat(tx.profit) >= 0 ? "profit" : "loss"}>
                                {tx.type === "SELL" ? tx.profit : "-"}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}

            <button onClick={handleNavigateHome} className="navigate-home-button">
                Return
            </button>
        </div>
    );
}
