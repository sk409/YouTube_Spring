import axios from "axios";

class Ajax {

    makeUrlWithQuery(url, query) {
        url += "?";
        for (let key in query) {
            const value = query[key];
            url += `${key}=${value}&`;
        }
        return url;
    }

    makeBody(data, config) {
        if (
            !config ||
            !config.headers
        ) {
            return data;
        }
        const contentType = config.headers["Content-Type"];
        let d = null;
        if (contentType === "multipart/form-data") {
            d = new FormData();
        } else if (contentType === "application/x-www-form-urlencoded") {
            d = new URLSearchParams();
        }
        for (const key in data) {
            d.append(key, data[key]);
        }
        return d;
    }

    get(url, data, config) {
        return axios.get(this.makeUrlWithQuery(url, data), config);
    }

    post(url, data, config) {
        return axios.post(url, this.makeBody(data, config), config);
    }

    put(url, data, config) {
        return axios.put(this.makeUrlWithQuery(url, data), config);
    }

    delete(url, data, config) {
        return axios.delete(this.makeUrlWithQuery(url, data), config);
    }
}

const ajax = new Ajax();

export default ajax;