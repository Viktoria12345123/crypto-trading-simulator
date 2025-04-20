import * as request from "./requester/requester.js";

const BASE_URL = "http://localhost:8081/api/crypto";

/**
 * Sells a specified amount of cryptocurrency.
 * @param {number} amount - The amount of cryptocurrency to sell.
 * @param {number} cost - The cost of selling the cryptocurrency.
 * @param {string} symbol - The cryptocurrency symbol (e.g., "BTC").
 * @param {string} name - The name of the cryptocurrency (e.g., "Bitcoin").
 * @returns {Promise<string>} - A message indicating the result of the transaction (e.g., "Sell successful").
 */
export const sell = async (amount, cost, symbol, name) => {
    return await request.post(`${BASE_URL}/sell`, { amount, cost, symbol, name });
}

/**
 * Buys a specified amount of cryptocurrency.
 * @param {number} amount - The amount of cryptocurrency to buy.
 * @param {number} cost - The cost of buying the cryptocurrency.
 * @param {string} symbol - The cryptocurrency symbol (e.g., "BTC").
 * @param {string} name - The name of the cryptocurrency (e.g., "Bitcoin").
 * @returns {Promise<string>} - A message indicating the result of the transaction (e.g., "Buy successful").
 */
export const buy = async (amount, cost, symbol, name) => {
    return await request.post(`${BASE_URL}/buy`, { amount, cost, symbol, name });
}


/**
 * Retrieves the current user's cryptocurrency holdings.
 * @returns {Promise<Holding[]>} - A list of holdings for the user.
 */
export const getHoldings = async () => {
    return await request.get(`${BASE_URL}/holdings`);
}

/**
 * Retrieves the current user's cryptocurrency transactions.
 * @returns {Promise<TransactionResponse[]>} - A list of transactions for the user.
 */
export const getTransactions = async () => {
    return await request.get(`${BASE_URL}/transactions`);
}


/**
 * @typedef {Object} TransactionResponse
 * @property {string} symbol - The cryptocurrency symbol (e.g., "BTC", "ETH").
 * @property {number} amount - The amount of cryptocurrency involved in the transaction.
 * @property {number} cost - The cost of the transaction.
 * @property {string} name - The name of the cryptocurrency (e.g., "Bitcoin", "Ethereum").
 * @property {string} type - The type of transaction (either "buy" or "sell").
 * @property {string} date - The date of the transaction.
 */

/**
 * @typedef {Object} Holding
 * @property {string} symbol - The cryptocurrency symbol (e.g., "BTC", "ETH").
 * @property {number} amount - The amount of cryptocurrency held.
 * @property {number} cost - The cost of holding the cryptocurrency.
 * @property {string} name - The name of the cryptocurrency (e.g., "Bitcoin", "Ethereum").
 */
