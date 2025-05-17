"use client"
import { useEffect, useState } from 'react';
import styles from './LoginForm.module.css'
import Image from 'next/image';

const LoginForm = ({onClose, onLogin}) => {
    const [codeWaiting, setCodeWaiting] = useState(false);

    const handleClickFormClose = (evt) => {
        if (evt.target.tagName === 'SECTION') {
            onClose();
        }
    };

    const handleLogin = () => {
        // onClose();
        // onLogin(true);
        setCodeWaiting(true);
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
                        <Image 
                            src='icons/logo.svg'
                            alt='Logo'
                            width={40}
                            height={40}
                        />
                        <h2>Введите почту</h2>
                        <p>Мы отправим одноразовый код.</p>
                        <input className={ `${styles.emailInput}` } placeholder='ivan.ivanov@mail.ru'/>
                        <button className={ `${styles.sender}` } onClick={() => handleLogin(true)}>Отправить код</button>
                        <a href='#' className={ `${styles.logInWithNumber}` }>Войти по номеру телефона</a>
                    </div>
                </form>
            </section>
        );
    } else {
        return (
            <section className={ `${styles.section}` } onClick={handleClickFormClose}>
                <form>
                    <div className={ `${styles.loginForm}` }>
                        <Image 
                            src='icons/logo.svg'
                            alt='Logo'
                            width={40}
                            height={40}
                        />
                        <h2>Введите одноразовый код</h2>
                        <p>Мы отправили одноразовый код на вашу почту.</p>
                        <input className={ `${styles.emailInput}` } placeholder='Код из письма' maxLength={6}/>
                        <a href='#' className={ `${styles.logInWithNumber}` }>Отправить код повторно</a>
                    </div>
                </form>
            </section>
        );
    }
};

export default LoginForm;