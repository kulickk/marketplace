import { addDommenToApi } from "./utils";

// MarketPlace Api
const DOMMEN = 'http://localhost:8080';

const marketplaceApi = {
    GOODS: {
        GET: '/api/v1/goods',
        GET_BY_ID: (id) => `/api/v1/goods/${id}`
    },
    GOODS_CATEGORY: {
        GET: '/api/v1/good-categories',
        GET_BY_ID: (id) => `/api/v1/good-categories/${id}`
    },
    AUTH: {
        REGISTER: '/api/v1/auth/register',
        LOGIN: '/api/v1/auth/login',
        CONFIRM: '/api/v1/auth/confirm',
        CHECK: '/api/v1/auth/some-endpoint'
    },
    USER: {
        REGISTER: '/api/users/register',
        GET_BY_ID: (id) => `/api/users/id/${id}`,
        GET_BY_EMAIL: (email) => `/api/users/email/${email}`
    }
};

const marketApi = addDommenToApi(marketplaceApi, DOMMEN);


export {marketApi}