"use client"
import Link from 'next/link';
import styles from './Header.module.css'
import LoginForm from '../LoginForm/LoginForm';
import { useState } from 'react';
import { useAuth } from '@/hooks/AuthContext';
import BasketSVG from '@/svg/CartSVG';
import OrdersSVG from '@/svg/OrdersSVG';
import SelectionMenuSVG from '@/svg/SelectionMenuSVG';
import LogoSVG from '@/svg/LogoSVG';
import SearchSVG from '@/svg/SearchSVG';
import CartSVG from '@/svg/CartSVG';

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
                        <LogoSVG />
                    </Link>
                </div>
                <button className={ `${styles.catalogue}` }>
                    <SelectionMenuSVG />
                    Каталог
                </button>
                <div className={ `${styles.searchInputContainer}` }>
                    <input className={ `${styles.searchInput}` } type="text" placeholder='Введите название...' />
                    <SearchSVG className={ `${styles.searchInputSvg}` } />
                </div>
                <Link className={ `${styles.categoryButton}` } href={'/basket'}>
                    <CartSVG />
                    Корзина
                </Link>
                <Link className={ `${styles.categoryButton}` } href={'/orders'}>
                    <OrdersSVG />
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