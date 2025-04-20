import * as request from "./requester/requester.js";

const BASE_URL = "http://localhost:8081/api/auth";

/**
 * @typedef {Object} SessionResponse
 * @property {number} id - The user's ID.
 * @property {string} username - The user's username.
 */

/**
 * Retrieves the current user session.
 * @returns {Promise<SessionResponse>} - The session data with user ID and username.
 */
export const register = async (username, password, rePass) => {
    return await request.post(`${BASE_URL}/register`, { username, password, rePass });
}

/**
 * Retrieves the current user session.
 * @returns {Promise<SessionResponse>} - The session data with user ID and username.
 */
export const login = async (username, password) => {
    return await request.post(`${BASE_URL}/login`, { username, password });
};

/**
 * Retrieves the current user session.
 * @returns {Promise<SessionResponse>} - The session data with user ID and username.
 */
export async function getSession() {
    return await request.get(`${BASE_URL}/session`);

}




