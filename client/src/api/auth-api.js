import * as request from "./requester/requester.js";

const BASE_URL = "http://localhost:8081/api/auth";

export const register = async (username, password, rePass) => {
    return await request.post(`${BASE_URL}/register`, { username, password, rePass });
}

export const login = async (username, password) => {
    return await request.post(`${BASE_URL}/login`, { username, password });
};

export async function getSession() {
    const session = await request.get(`${BASE_URL_SPRING}/session`);
    return session;
}




