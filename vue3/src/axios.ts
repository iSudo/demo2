import * as axios from "axios";

const axiosInstance = axios.default.create({
    baseURL: "http://localhost:8080"
});

export default axiosInstance;