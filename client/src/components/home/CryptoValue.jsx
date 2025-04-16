export default function CryptoValue() {
    return (
        <div className="crypto-showcase">
            <div className="crypto-item">
                <div className="crypto-icon-wrapper">
                    <i className="fa-solid fa-bitcoin-sign"></i>
                </div>
                <div className="crypto-info">
                    <h3>Bitcoin (BTC)</h3>
                </div>
                <p className="crypto-price">$45,000</p>
            </div>
            <div className="crypto-item">
                <div className="crypto-icon-wrapper">
                    <i className="fa-solid fa-bitcoin-sign"></i>
                </div>
                <div className="crypto-info">
                    <h3>Ethereum (ETH)</h3>
                </div>
                <p className="crypto-price">$3,000</p>
            </div>
        </div>
    );
}
