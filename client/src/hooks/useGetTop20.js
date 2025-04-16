import { useState, useEffect } from 'react';
import * as requester from "../api/requester/requester.js";

const top20Cryptocurrencies = [
    { symbol: 'XXBT', name: 'Bitcoin' },
    { symbol: 'XETH', name: 'Ethereum' },
    { symbol: 'USDT', name: 'Tether' },
    { symbol: 'USDC', name: 'USD Coin' },
    { symbol: 'SOL', name: 'Solana' },
    { symbol: 'ADA', name: 'Cardano' },
    { symbol: 'PAXG', name: 'Paxos Gold' },
    { symbol: 'CRV', name: 'Curve DAO Token' },
    { symbol: 'XCN', name: 'Chain' },
    { symbol: 'FARTCOIN', name: 'FartCoin' },
    { symbol: 'DOGE', name: 'Dogecoin' },
    { symbol: 'POPCAT', name: 'Popcat' },
    { symbol: 'TAO', name: 'TAO Token' },
    { symbol: 'PROMPT', name: 'Prompt Token' },
    { symbol: 'PEPE', name: 'PepeCoin' },
    { symbol: 'SPX', name: 'S&P 500 Index' },
    { symbol: 'ONDO', name: 'Ondo Finance' },
    { symbol: 'ZUSD', name: 'USD Digital' },
    { symbol: 'XXRP', name: 'Ripple (XRP)' },
    { symbol: 'LINK', name: 'Chainlink' }
];

const symbolMapping = {
    'XXBT': 'BTC',
    'XETH': 'ETH',
    'XLTC': 'LTC',
    'XREP': 'REP',
    'XTZ': 'XTZ',
    'XXDG': 'XDG',
    'XETC': 'ETC',
    'XMLN': 'MLN',
    'XXRP': 'XRP',
    'XMR': 'XMR'
};


const useTop20Currencies = () => {
    const [top20Currencies, setTop20Currencies] = useState([]);

    const fetchAssetPairs = async () => {
        const response = await requester.get("https://api.kraken.com/0/public/AssetPairs");
        const assetPairs = response.result;
        const assetPairMap = {};

        for (const pair in assetPairs) {
            const baseName = assetPairs[pair].base;
            let altName = assetPairs[pair].altname;
            assetPairMap[altName] = baseName;
        }

        return assetPairMap;
    };

    const fetchAllTickerData = async () => {
        const data = await requester.get('https://api.kraken.com/0/public/Ticker');
        return data.result || {};
    };

    const fetchTop20Currencies = async () => {
        try {
            const allTickers = await fetchAllTickerData();
            const assetPairs = await fetchAssetPairs();
            const pairsWithVolume = [];

            for (const pair in allTickers) {
                const pairData = allTickers[pair];

                const volume = parseFloat(pairData.v[1]);
                const price = parseFloat(pairData.c[0]);

                let altname = assetPairs[pair];
                const symbol = symbolMapping[altname] || altname;

                const fullCoinName = coinNameMapping[symbol] || symbol;
                let currency = pair.slice(-3);
                const usdVolume = volume * price;
                const formattedPrice = price.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 8 });

                if (currency === "SDT" || currency === "SDC") {
                    currency = "USD";
                }

                if (symbol !== undefined && currency === "USD") {
                    pairsWithVolume.push({
                        pair,
                        volume: usdVolume,
                        price: formattedPrice,
                        name: fullCoinName,
                        altname,
                        symbol,
                        currency
                    });
                }
            }

            const sortedPairs = pairsWithVolume.sort((a, b) => b.volume - a.volume);
            const top20Pairs = sortedPairs.slice(0, 20);
            setTop20Currencies(top20Pairs);

        } catch (error) {
            console.error("Error fetching top currencies:", error);
        }
    };

    useEffect(() => {
        fetchTop20Currencies();
    }, []);

    return top20Currencies;
};

export default useTop20Currencies;
