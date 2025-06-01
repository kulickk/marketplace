import { marketApi } from "@/utils/const";

const sendCode = async (email) => {
    const requestData = {
            "email": email,
            "password": "Qwerty123!",
            "firstName": "Иван",
            "lastName": "Петров"
        };

    console.log(requestData);
    console.log(JSON.stringify(requestData));

    fetch(marketApi.AUTH.REGISTER, {
        method: 'POST',
        body: JSON.stringify(requestData),
        headers: {
            'Content-Type': 'application/json',
        }
    }).then(response => {
        if (!response.ok) {
            return response.json().then(errorData => {
                const error = new Error(errorData.message || 'HTTP Error');
                error.status = response.status;
                throw error;
            });
        }

        return response.json();
    }).then(data => {
        console.log(data);
    });
};

export {sendCode};