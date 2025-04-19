import * as request from "./requester/requester.js";

const BASE_URL = "http://localhost:8081/api/users";


export const getBalance = async() => {
    return await request.get(`${BASE_URL}/balance`);
}

export const reset = async() => {
    return await request.post(`${BASE_URL}/reset`);
}