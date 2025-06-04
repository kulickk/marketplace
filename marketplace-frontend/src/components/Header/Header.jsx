"use client"
import Link from 'next/link';
import styles from './Header.module.css'
import LoginForm from '../LoginForm/LoginForm';
import { useState } from 'react';
import { useAuth } from '@/hooks/AuthContext';
import OrdersSVG from '@/svg/OrdersSVG';
import SelectionMenuSVG from '@/svg/SelectionMenuSVG';
import LogoSVG from '@/svg/LogoSVG';
import SearchSVG from '@/svg/SearchSVG';
import CartSVG from '@/svg/CartSVG';
import { frontendRouter } from '@/utils/const';
import CustomButton from '../CustomButton/CustomButton';
import { useRouter } from 'next/navigation';

const Header = () => {
    const [isLoginFormShown, setIsLoginFormShown] = useState(false);
    const {isAuthenticated, setIsAuthenticated} = useAuth();
    const router = useRouter();

    const onLoginFormOpen = (evt) => {
        evt.preventDefault();
        setIsLoginFormShown(true);
    };

    const onLoginFormClose = () => {
        setIsLoginFormShown(false);
    };

    const handleToProfileClick = () => {
        router.push(frontendRouter.ME);
    };

    return (
        <>
            <div className={ `${styles.main}` }>
                <div className={ `${styles.logo}` }>
                    <Link href={frontendRouter.INDEX}>
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
                <Link className={ `${styles.categoryButton}` } href={frontendRouter.CART}>
                    <CartSVG />
                    Корзина
                </Link>
                <Link className={ `${styles.categoryButton}` } href={frontendRouter.ORDERS}>
                    <OrdersSVG />
                    Заказы
                </Link>
                { (!isAuthenticated) ? 
                <CustomButton className={ `${styles.signInButton}` } onClick={onLoginFormOpen}>Войти</CustomButton> : 
                <CustomButton className={ `${styles.signInButton}` } onClick={handleToProfileClick}>Личный кабинет</CustomButton>}
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