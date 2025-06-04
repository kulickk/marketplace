import { marketApi } from '@/utils/const';

const sendCode = async (email, setLoginId) => {
    const requestData = {
            'email': email,
            'password': 'Qwerty123!',
            'firstName': 'Иван',
            'lastName': 'Петров'
        };

    fetch(marketApi.AUTH.REGISTER, {
        method: 'POST',
        body: JSON.stringify(requestData),
        headers: {
            'Content-Type': 'application/json',
        }
    }).then(response => {
        console.log(response);
        if (!response.ok) {
            if (response.status === 409) {
                const {firstName, lastName, ...requestDataLogin} = requestData;
                fetch(marketApi.AUTH.LOGIN, {
                    method: 'POST',
                    body: JSON.stringify(requestDataLogin),
                    headers: {
                        'Content-Type': 'application/json',
                    }
                }).then(response => {
                    if (response.ok && (response.status !== 429)) {
                        return response.json();
                    }
                    throw new Error('Too many requests');
                }).then(data => setLoginId(data.loginId));
            }
        }

        return response.json();
    }).then(data => {
        console.log(data);
    });
};

export {sendCode};