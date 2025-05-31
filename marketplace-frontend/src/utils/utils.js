const addDommenToApi = (apiObj, dommen) => {
    const processed = {};

    for (const key in apiObj) {
        const value = apiObj[key];

        if (typeof value === 'string') {
            processed[key] = dommen + value;
        } else if (typeof value === 'function') {
            processed[key] = (...args) => dommen + value(...args);
        } else if (typeof value === 'object' && value !== null) {
            processed[key] = addDommenToApi(value, dommen);
        } else {
            processed[key] = value;
        }
    }

    return processed;
};

export {addDommenToApi};