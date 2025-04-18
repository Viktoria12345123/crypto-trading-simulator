import axios from 'axios';

async function requester(method, url, data) {

        const response = await axios({
            method,
            url,
            data,
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            },
        });

        return response;
}

export const get = (url, data) => requester('GET', url, data);
export const post = (url, data) => requester('POST', url, data);
export const put = (url, data) => requester('PUT', url, data);
export const del = (url, data) => requester('DELETE', url, data);