"use client"
import Link from 'next/link';
import Image from 'next/image';
import SvgIcon from '../SvgImage';
import styles from './Header.module.css'
import LoginForm from '../LoginForm/LoginForm';
import { useState } from 'react';
import { useAuth } from '@/hooks/AuthContext';

const Header = () => {
    const [isLoginFormShown, setIsLoginFormShown] = useState(false);
    const {isAuthenticated, setIsAuthenticated} = useAuth();

    const onLoginFormOpen = (evt) => {
        evt.preventDefault();
        setIsLoginFormShown(true);
    };

    const onLoginFormClose = () => {
        setIsLoginFormShown(false);
    };

    return (
        <>
            <div className={ `${styles.main}` }>
                <div className={ `${styles.logo}` }>
                    <Link href={'/'}>
                        <Image 
                            src='icons/logo.svg'
                            alt='Logo'
                            width={50}
                            height={50}
                        />
                    </Link>
                </div>
                <button className={ `${styles.catalogue}` }>
                    <SvgIcon
                        src='icons/selection-menu.svg'
                    />
                    Каталог
                </button>
                <div className={ `${styles.searchInputContainer}` }>
                    <input className={ `${styles.searchInput}` } type="text" placeholder='Введите название...' />
                    <SvgIcon 
                        src='icons/search.svg'
                        className={ `${styles.searchInputSvg}` }
                    />
                </div>
                <Link className={ `${styles.categoryButton}` } href={'/basket'}>
                    <Image 
                        src='icons/basket.svg'
                        alt='Basket'
                        width={25}
                        height={19}
                    />
                    Корзина
                </Link>
                <Link className={ `${styles.categoryButton}` } href={'/orders'}>
                    <Image 
                        src='icons/orders.svg'
                        alt='Orders'
                        width={25}
                        height={19}
                    />
                    Заказы
                </Link>
                { (!isAuthenticated) ? 
                <button className={ `${styles.signInButton}` } onClick={onLoginFormOpen}>Войти</button> : 
                <button className={ `${styles.signInButton}` }>Личный кабинет</button>}
            </div>
            { (isLoginFormShown) ? 
            <LoginForm 
            onClose={onLoginFormClose}
            onLogin={setIsAuthenticated}
            /> : '' }
        </>
    );
};

export default Header;