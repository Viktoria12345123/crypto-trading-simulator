import * as request from "./requester/requester.js";

const BASE_URL = "http://localhost:8081/api/crypto";

export const sell = async (amount, cost, symbol, name) => {
    return await request.post(`${BASE_URL}/sell`, { amount, cost, symbol, name });
}

export const buy = async (amount, cost, symbol, name) => {
    return await request.post(`${BASE_URL}/buy`, { amount, cost, symbol, name });
}

export const getHoldings = async () => {
    return await request.get(`${BASE_URL}/holdings`);
}

export const getTransactions = async () => {
    return await request.get(`${BASE_URL}/transactions`);
}





