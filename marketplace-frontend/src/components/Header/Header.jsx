"use client"
import Link from 'next/link';
import styles from './Header.module.css'
import LoginForm from '../LoginForm/LoginForm';
import { useRef, useState } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import OrdersSVG from '@/svg/OrdersSVG';
import SelectionMenuSVG from '@/svg/SelectionMenuSVG';
import LogoSVG from '@/svg/LogoSVG';
import SearchSVG from '@/svg/SearchSVG';
import CartSVG from '@/svg/CartSVG';
import { frontendRouter } from '@/utils/const';
import CustomButton from '../CustomButton/CustomButton';
import { redirect, useRouter } from 'next/navigation';
import Catalogue from '../Catalogue/Catalogue';
import { getUserInfo } from '@/utils/requests';

const Header = () => {
    const [isLoginFormShown, setIsLoginFormShown] = useState(false);
    const [isCatalogueShown, setIsCatalogueShown] = useState(false);
    const {isAuthenticated, setIsAuthenticated, cartCount, ordersCount, setOrdersCount, setCartCount, setUserRole, setUserName} = useAuth();
    const router = useRouter();
    const searchInput = useRef();

    const onLoginFormOpen = (evt) => {
        evt.preventDefault();
        setIsLoginFormShown(true);
    };

    const onLoginFormClose = () => {
        setIsLoginFormShown(false);
    };

    const onCatalogueOpen = (evt) => {
        evt.preventDefault();
        setIsCatalogueShown(true);
    }

    const onCatalogueClose = () => {
        setIsCatalogueShown(false);
    }

    const handleToProfileClick = () => {
        router.push(frontendRouter.ME);
    };

    const handleLogin = () => {
        getUserInfo({setIsAuthenticated, setUserRole, setCartCount, setOrdersCount, setUserName});
    };

    const onSearch = (evt) => {
        console.log(searchInput.current);
        evt.preventDefault();
        redirect(frontendRouter.SEARCH(searchInput.current.value));
    };

    return (
        <>
            <div className={ `${styles.main}` }>
                <div className={ `${styles.logo}` }>
                    <Link href={frontendRouter.INDEX}>
                        <LogoSVG />
                    </Link>
                </div>
                <button className={ `${styles.catalogue}` } onClick={onCatalogueOpen}>
                    <SelectionMenuSVG />
                    Каталог
                </button>
                <div className={ `${styles.searchInputContainer}` }>
                    <form onSubmit={onSearch}>
                        <input className={ `${styles.searchInput}` } type="text" placeholder='Введите название...' ref={searchInput}/>
                        <SearchSVG className={ `${styles.searchInputSvg}` } />
                    </form>
                </div>
                <Link className={ `${styles.categoryButtonContainer}` } href={frontendRouter.CART}>
                    <div className={ `${styles.categoryButton}` }>
                        <CartSVG />
                        Корзина
                    </div>
                    { (isAuthenticated) ? <>
                    <div className={ `${styles.categoryCount}` }>
                        <p>{cartCount}</p>
                    </div>
                    </> : ''}
                </Link>
                <Link className={ `${styles.categoryButtonContainer}` } href={frontendRouter.ORDERS}>
                    <div className={ `${styles.categoryButton}` }>
                        <OrdersSVG />
                        Заказы
                    </div>
                    { (isAuthenticated) ? <>
                    <div className={ `${styles.categoryCount}` }>
                        <p>{ordersCount}</p>
                    </div>
                    </> : ''}
                </Link>
                { (isAuthenticated === null) ? <CustomButton className={ `${styles.signInButton}` }></CustomButton>: 
                    (!isAuthenticated) ? 
                    <CustomButton className={ `${styles.signInButton}` } onClick={onLoginFormOpen}>Войти</CustomButton> : 
                    <CustomButton className={ `${styles.signInButton}` } onClick={handleToProfileClick}>Личный кабинет</CustomButton>
                }
            </div>
            { (isCatalogueShown) ?
            <Catalogue 
            onClose={onCatalogueClose}
            /> : ''
            }
            { (isLoginFormShown) ? 
            <LoginForm 
            onClose={onLoginFormClose}
            onLogin={handleLogin}
            /> : '' }
        </>
    );
};

export default Header;