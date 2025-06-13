const ORDER_DATE_FORMAT = 'DD.MM.YYYY';

// MarketPlace Api
const DOMMEN = 'http://localhost:8080';

const marketApi = {
    GOODS: {
        GET: `${DOMMEN}/api/v1/goods`,
        CREATE: `${DOMMEN}/api/v1/goods`,
        GET_BY_ID: (id) => `${DOMMEN}/api/v1/goods/${id}`,
        GET_MY_GOODS: `${DOMMEN}/api/v1/goods/get-my-goods`,
        SEARCH_BY_NAME: (productName) => `${DOMMEN}/api/v1/goods/search?query=${productName}`,
        SEARCH_BY_CATEGORY: (categoryId) => `${DOMMEN}/api/v1/goods/category/${categoryId}`,
        UPLOAD_IMAGE: (goodId) => `${DOMMEN}/api/v1/goods/${goodId}/images`
    },
    GOODS_CATEGORY: {
        GET: `${DOMMEN}/api/v1/good-categories`,
        GET_BY_ID: (id) => `${DOMMEN}/api/v1/good-categories/${id}`,
        GOODS: (categoryId) => `${DOMMEN}/api/v1/good-categories/${categoryId}`
    },
    AUTH: {
        REGISTER: `${DOMMEN}/api/v1/auth/register`,
        LOGIN: `${DOMMEN}/api/v1/auth/login`,
        CONFIRM: `${DOMMEN}/api/v1/auth/confirm`,
        CHECK: `${DOMMEN}/api/v1/auth/some-endpoint`,
        LOGOUT: `${DOMMEN}/api/v1/auth/logout`
    },
    USER: {
        PROFILE: `${DOMMEN}/api/v1/users/me/profile`,
        PASSWORD: `${DOMMEN}/api/v1/users/me/password`,
        ME: `${DOMMEN}/api/v1/users/me`
    },
    BASKET: {
        GET: `${DOMMEN}/api/v1/me/basket`,
        ADD_GOOD: `${DOMMEN}/api/v1/me/basket/items`,
        DELETE_GOOD: (id) => `${DOMMEN}/api/v1/me/basket/items/${id}`,
        UPDATE_QUANTITY: (id) => `${DOMMEN}/api/v1/me/basket/items/${id}`
    },
    ORDERS: {
        GET: `${DOMMEN}/api/v1/orders`,
        CREATE: `${DOMMEN}/api/v1/orders`,
    },
    GET_GOOD_PHOTO: (id) => `${DOMMEN}/uploads/goods/${id}`,
    ADMIN: {
        PROMOTE_SELLER: (id) => `${DOMMEN}/api/v1/admin/users/${id}/promote-seller`,
        USERS: `${DOMMEN}/api/v1/admin/users`
    }
};

// Forntend Routes
const frontendRouter = {
    INDEX: `/`,
    CART: `/my/cart`,
    ORDERS: `/my/orders`,
    PRODUCT: (id) => `/product/${id}`,
    ME: `/my/account`,
    SEARCH: (product) => `/search/${product}`,
    SEARCH_CATEGORY: (category) => `/category-search/${category}`
};


const USER_ROLE = {
    USER: 'BUYER',
    SELLER: 'SELLER',
    ADMIN: 'ADMIN'
};

const USER_ROLE_NAME = {
    BUYER: 'покупателя',
    SELLER: 'продавца',
    ADMIN: 'администратора'
};

const ACCOUNT_CONTENT = {
    PRESONAL_INFORMATION: 'PRESONAL_INFORMATION',
    REVIEWS: 'REVIEWS',
    MY_GOODS: 'MY_GOODS',
    ANALITICS: 'ANALITICS',
    USERS: 'USERS'
};

const MY_GOOD_FORM = {
    EDIT: 'EDIT',
    CREATE: 'CREATE'
}

export {marketApi, frontendRouter, ORDER_DATE_FORMAT, USER_ROLE, USER_ROLE_NAME, ACCOUNT_CONTENT}