'use client'
import ProductCardCart from '@/components/ProductCard/ProductCardInCart/ProductCardInCart';
import styles from './page.module.css'
import { useEffect, useMemo, useState } from 'react';
import { getFormattedPrice, getRandomInt } from '@/utils/utils';
import CustomButton from '@/components/CustomButton/CustomButton';

const getGoodsCards = (goods, onSelect, onIncreaseCount, onDecreaseCount, onDelete) => {
    const cards = goods.map(({id, desc, price, selected}) => {
        return(
            <ProductCardCart
            id={id}
            desc={desc}
            price={price}
            onSelect={onSelect}
            onIncreaseCount={onIncreaseCount}
            onDecreaseCount={onDecreaseCount}
            onDelete={onDelete}
            selected={selected}
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

const Cart = () => {
    const [goods, setGoods] = useState([]);
    const [selectedTotal, setSelectedTotal] = useState(0);
    const [selectedGoodsCount, setSelectedGoodsCount] = useState(0);

    const countTotal = (goodsList) => {
        let total = 0;
        goodsList.forEach(good => {
            if (good.selected) {
                total += good.price * good.count
            }
        });
        setSelectedTotal(total);
    };

    const countSelectedGoods = (goodsList) => {
        let total = 0;
        goodsList.map(good => {
            if (good.selected) {
                total += good.count;
            }
        });
        setSelectedGoodsCount(total);
    };

    const handleCardToggle = (goodId) => {
        setGoods(goods.map(good => {
            if (good.id !== goodId) return good;
            if (!good.selected) {
                setSelectedTotal(selectedTotal + good.price * good.count);
                setSelectedGoodsCount(selectedGoodsCount + good.count);
            } else {
                setSelectedTotal(selectedTotal - good.price * good.count);
                setSelectedGoodsCount(selectedGoodsCount - good.count);
            }
            return { ...good, selected: !good.selected };
        }))
    };

    const handleSelectAll = () => {
        const goodsSelectedList = goods.map(good => ({ ...good, selected: true }));
        setGoods(goodsSelectedList);
        countTotal(goodsSelectedList);
        countSelectedGoods(goodsSelectedList);
    };

    const deleteSelected = () => {
        setGoods(goods.map(good => ({ ...good, selected: false })));
        setSelectedTotal(0);
        setSelectedGoodsCount(0);
    };

    const handleDeleteSelected = (evt) => {
        evt.preventDefault();
        deleteSelected();
    };

    const increaseGoodCount = (goodId) => {
        setGoods(goods.map(good => {
            if ((good.id !== goodId) || (good.count + 1 > 999)) return good;
            if (good.selected) {
                setSelectedTotal(selectedTotal + good.price);
                setSelectedGoodsCount(selectedGoodsCount + 1);
            }
            return { ...good, count: good.count + 1 };
        }))
    };

    const decreaseGoodCount = (goodId) => {
        setGoods(goods.map(good => {
            if ((good.id !== goodId) || (good.count - 1 < 0)) return good;
            if (good.selected) {
                setSelectedTotal(selectedTotal - good.price);
                setSelectedGoodsCount(selectedGoodsCount - 1);
            }
            return { ...good, count: good.count - 1 };
        }))
    };

    const deleteGood = (goodId) => {
        const goodsFilteredList = goods.filter(good => good.id !== goodId);
        setGoods(goodsFilteredList);
        countSelectedGoods(goodsFilteredList);
        countTotal(goodsFilteredList);
    };

    useEffect(() => {
        setGoods(goodsMock.map(good => ({
            ...good,
            selected: false,
            count: 1
        })))
    }, [])

    const allSelected = useMemo(() => {
        return goods.length > 0 && goods.every(good => good.selected);
    }, [goods]);

    if (goods) {
        return(
        <>
            <h1 className={`${styles.pageHeader}`}>Корзина</h1>
            <div className={`${styles.pageContentContainer}`}>
                <div className={`${styles.goodsSelectionContainer}`}>
                    <div className={`${styles.selectionTopPanel}`}>
                        <div className={`${styles.selectAllContainer}`}>
                            <input type="checkbox" id='select-all'  className={`${styles.selectAllInput}`} onClick={() => !allSelected ? handleSelectAll() : deleteSelected()} onChange={() => allSelected ? handleSelectAll : deleteSelected} checked={allSelected}/>
                            <label htmlFor="select-all" className={`${styles.selectAll}`}>Выбрать всё</label>
                        </div>
                        <a href='#' className={`${styles.deleteSelected}`} onClick={handleDeleteSelected}>Удалить выбранные</a>
                    </div>
                    <div className={`${styles.goodsContainer}`}>
                        {getGoodsCards(goods, handleCardToggle, increaseGoodCount, decreaseGoodCount, deleteGood)}
                    </div>
                </div>
                <div className={`${styles.totalContainer}`}>
                    <p className={`${styles.totalContainerTitle}`}>Ваша корзина</p>
                    <div className={`${styles.totalPriceContainer}`}>
                        <p className={`${styles.totalGoods}`}>Товары ({selectedGoodsCount})</p>
                        <p className={`${styles.totalPrice}`}>{getFormattedPrice(selectedTotal)} ₽</p>
                    </div>
                    <CustomButton className={`${styles.checkoutButton}`} onClick={() => console.log(selectedGoodsCount, selectedTotal, goods)}>Перейти к оформлению</CustomButton>
                </div>
            </div>
        </>
    );
    }
};

export default Cart;