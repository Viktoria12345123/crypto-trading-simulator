export default function CurrencyItem({ symbol, displayName, price, currency }) {

    const formattedPrice = (price) => {
        const number = parseFloat(price);
        if (isNaN(number)) return price;
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: currency,
            minimumFractionDigits: 2,
            maximumFractionDigits: 4,
        }).format(number);
    };

    return (
        <div className="crypto-item">
            <div className="crypto-icon-wrapper">
                <i className="fa-solid fa-bitcoin-sign"></i>
            </div>
            <div className="crypto-info">
                <h3>{displayName} ({symbol})</h3>
            </div>
            <p className="crypto-price">{formattedPrice(price)} {currency}</p>
        </div>
    );
}
