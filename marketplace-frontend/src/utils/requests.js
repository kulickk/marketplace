const { marketApi, frontendRouter } = require("./const");

const logout = (redirect, isAuthentificated) => {
    fetch(marketApi.AUTH.LOGOUT, {
        method: 'POST',
        credentials: 'include'
    }).then(response => {
        if (response.ok) {
            isAuthentificated(false);
            redirect(frontendRouter.INDEX);
        }
    });
};

export {logout}