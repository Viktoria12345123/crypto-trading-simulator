import { useEffect, useRef, useState } from "react";

export function useWebSocketPrices(top20Cryptocurrencies, pricesRef) {

    const [dummyUpdate, setDummyUpdate] = useState(0);

    useEffect(() => {
        const websocket = new WebSocket('wss://ws.kraken.com');

        websocket.onopen = () => {
            const pairs = top20Cryptocurrencies.map(crypto => `${crypto.symbol.toUpperCase()}/USD`);
            websocket.send(JSON.stringify({
                event: 'subscribe',
                pair: pairs,
                subscription: { name: 'book', depth: 10 }
            }));
        };

        websocket.onmessage = (event) => {
            const message = JSON.parse(event.data);
            if (Array.isArray(message) && message[1] && message[1].a) {
                const pair = message[3];
                const askPrice = message[1].a[0][0];
                pricesRef.current[pair] = askPrice;
                setDummyUpdate(d => d + 1);
            }
        };

        websocket.onclose = () => {
            console.log('WebSocket connection closed');
        };

        return () => {
            websocket.close();
        };
    }, [top20Cryptocurrencies]);

    return { dummyUpdate };
}
