import * as request from "./requester/requester.js";

const BASE_URL = "http://localhost:8081/api/users";


export const getBalance = async() => {
    return await request.get(`${BASE_URL}/balance`);
}


/**
 * Resets the current user's account.
 *
 * Sends a POST request to `http://localhost:8081/api/users/reset`.
 * This endpoint deletes all user data (transactions, holdings) and resets
 * the balance to the default value (e.g., 10,000).
 *
 * @async
 * @function reset
 * @returns {Promise<Object>} A promise that resolves to the response from the reset operation.
 */
export const reset = async() => {
    return await request.post(`${BASE_URL}/reset`);
}