'use client'
import { useEffect, useState } from 'react';
import styles from './LoginForm.module.css'
import { sendCode } from './requests';
import { marketApi } from '@/utils/const';
import LogoSVG from '@/svg/LogoSVG';
import CustomButton from '../CustomButton/CustomButton';

const LoginForm = ({onClose, onLogin}) => {
    const [email, setEmail] = useState('');
    const [inputValue, setInputValue] = useState('');
    const [codeWaiting, setCodeWaiting] = useState(false);
    const [loginId, setLoginId] = useState('');

    const handleClickFormClose = (evt) => {
        if (evt.target.tagName === 'SECTION') {
            onClose();
        }
    };

    const handleLogin = () => {
        setEmail(inputValue);
        sendCode(inputValue, setLoginId);
        setCodeWaiting(true);
        setInputValue('');
    };

    const handleInput = (evt) => {
        setInputValue(evt.target.value);
    };

    const sendCodeAgain = () => {
        const requestData = {
            email: email,
            password: 'Qwerty123!'
        };

        fetch(marketApi.AUTH.LOGIN, {
            method: 'POST',
            body: JSON.stringify(requestData),
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json',
            }
        }).then(response => {
            if (response.ok && (response.status !== 429)) {
                return response.json();
            }
            throw new Error('Too many requests');
        }).then(data => setLoginId(data.loginId));
    };

    const handleSendCodeAgain = (evt) => {
        evt.preventDefault();
        sendCodeAgain();
    };

    const confirmOTP = () => {
        fetch(marketApi.AUTH.CONFIRM, {
            method: 'POST',
            credentials: 'include',
            body: JSON.stringify({
                "loginId": loginId,
                "otp": inputValue
            }),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (!response) {
                throw new Error('Wrong OTP');
            }
            onLogin(true);
            onClose();
        });
    };

    const handleConfirmOTP = (evt) => {
        evt.preventDefault();
        confirmOTP();
    };

    useEffect(() => {
        const escKeydownHandler = (evt) => {
            if (evt.key === 'Escape' || evt.key === 'Esc') {
                evt.preventDefault();
                onClose();
            }
        };

        document.addEventListener('keydown', escKeydownHandler);
        return () => document.removeEventListener('keydown', escKeydownHandler);
    }, []);

    if (!codeWaiting) {
        return (
            <section className={ `${styles.section}` } onClick={handleClickFormClose}>
                <form>
                    <div className={ `${styles.loginForm}` }>
                        <LogoSVG 
                        width={40}
                        height={40}
                        />
                        <h2>Введите почту</h2>
                        <p>Мы отправим одноразовый код.</p>
                        <input className={ `${styles.emailInput}` } type='email' placeholder='ivan.ivanov@mail.ru' onChange={handleInput} value={inputValue}/>
                        <CustomButton className={ `${styles.sender}` } onClick={() => handleLogin()}>Отправить код</CustomButton>
                        <a href='#' className={ `${styles.logInWithNumber}` }>Войти по номеру телефона</a>
                    </div>
                </form>
            </section>
        );
    } else {
        return (
            <section className={ `${styles.section}` } onClick={handleClickFormClose}>
                <form onSubmit={handleConfirmOTP}>
                    <div className={ `${styles.loginForm}` }>
                        <LogoSVG 
                        width={40}
                        height={40}
                        />
                        <h2>Введите одноразовый код</h2>
                        <p>Мы отправили одноразовый код на вашу почту.</p>
                        <input className={ `${styles.emailInput}` } onChange={handleInput} placeholder='Код из письма' maxLength={6} value={inputValue}/>
                        <a href='#' onClick={handleSendCodeAgain} className={ `${styles.logInWithNumber}` }>Отправить код повторно</a>
                    </div>
                </form>
            </section>
        );
    }
};

export default LoginForm;