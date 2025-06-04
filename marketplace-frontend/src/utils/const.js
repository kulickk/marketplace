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
        CHECK: '/api/v1/auth/some-endpoint',
        LOGOUT: '/api/v1/auth/logout'
    },
    USER: {
        PROFILE: '/api/v1/users/me/profile',
        PASSWORD: '/api/v1/users/me/password',
        ME: '/api/v1/users/me'
    }
};

const marketApi = addDommenToApi(marketplaceApi, DOMMEN);

// Forntend Routes
const frontendRouter = {
    INDEX: '/',
    CART: '/my/cart',
    ORDERS: '/my/orders',
    PRODUCT: (id) => `/product/${id}`,
    ME: '/my/account'
};

export {marketApi, frontendRouter}