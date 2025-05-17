const escHandler = (callback) => {
    const escKeydownHandler = (evt) => {
        console.log(12345);
    if (evt.key === 'Escape' || evt.key === 'Esc') {
        evt.preventDefault();
        console.log(123);
    }
    return escKeydownHandler;
};
};

export {escHandler};