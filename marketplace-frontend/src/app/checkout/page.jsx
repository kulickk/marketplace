'use client'
import { useState } from 'react';
import styles from './page.module.css'
import CustomButton from '@/components/CustomButton/CustomButton';
import { getFormattedPrice, getRandomInt } from '@/utils/utils';
import ProductCardCart from '@/components/ProductCard/ProductCardInCart/ProductCardInCart';
import Image from 'next/image';


const getGoodsCards = (goods) => {
    const cards = goods.map(({id, desc, price}) => {
        return(
            <ProductCardCart
            id={id}
            desc={desc}
            price={price}
            isOrder={true}
            key={id}
            />
        );
    });
    return cards;
};

const goodsMock = [
    {
        id: '1',
        desc: 'Кроссовки Adidas Sportswear CRAZYCdasasdasdas dasdasdasdasdasdasdasd asdasdasdasdasd asdasddasasdasdasdasddas',
        price: getRandomInt(10000),
    },
    {
        id: '2',
        desc: 'Кроссовки Adidas Sportswear JDKLASJKS',
        price: getRandomInt(10000),
    },
    {
        id: '3',
        desc: 'Кроссовки Adidas Sportswear EIOPWA',
        price: getRandomInt(10000),
    },
    {
        id: '4',
        desc: 'Кроссовки Adidas Sportswear BVCJN<M',
        price: getRandomInt(10000),
    }
];

const CheckOutPage = () => {
    const [goods, setGoods] = useState(goodsMock);

    return(
        <div className={ `${styles.pageContainer}` }>
            <h1 className={`${styles.pageHeader}`}>Оформление заказа</h1>
            <div className={`${styles.pageContentContainer}`}>
                <div className={`${styles.goodsSelectionContainer}`}>
                    <div className={`${styles.selectionTopPanel}`}>
                        {/* <div className={`${styles.selectAllContainer}`}>
                            <input type="checkbox" id='select-all'  className={`${styles.selectAllInput}`} />
                            <label htmlFor="select-all" className={`${styles.selectAll}`}>Выбрать всё</label>
                        </div>
                        <a href='#' className={`${styles.deleteSelected}`}>Удалить выбранные</a> */}
                        <h2 className={`${styles.paymentTypeTitle}`}>Способ оплаты</h2>
                        <div className={`${styles.paymentTypeContainer}`}>
                            <Image 
                            className={ `${styles.cardImage}` }
                            src='/images/Payment 1.png'
                            alt='Payment'
                            width={150}
                            height={80}
                            />
                            <Image 
                            className={ `${styles.cardImage}` }
                            src='/images/Payment 2.png'
                            alt='Payment'
                            width={150}
                            height={80}
                            />
                        </div>
                    </div>
                    <div className={`${styles.selectionTopPanel}`}>
                        <h2 className={`${styles.paymentTypeTitle}`}>Способ получения</h2>
                    </div>
                    <div className={`${styles.goodsContainer}`}>
                        {/* {getGoodsCards(goods)} */}
                    </div>
                </div>
                <div className={`${styles.totalContainer}`}>
                    <p className={`${styles.totalContainerTitle}`}>Ваша корзина</p>
                    <div className={`${styles.totalPriceContainer}`}>
                        <p className={`${styles.totalGoods}`}>Товары (0)</p>
                        <p className={`${styles.totalPrice}`}>1 777 ₽</p>
                    </div>
                    <CustomButton className={`${styles.checkoutButton}`}>Оплатить онлайн</CustomButton>
                </div>
            </div>
        </div>
    );
};

export default CheckOutPage;