import DeleteSVG from '@/svg/DeleteSVG';
import styles from './ProductCardInCart.module.css'
import Image from 'next/image';
import { getFormattedPrice } from '@/utils/utils';
import { useState } from 'react';

const ProductCardCart = ({id, desc, price, onSelect, onIncreaseCount, onDecreaseCount, onDelete, selected}) => {
    const [count, setCount] = useState(1);
    const title = desc.slice(0, 34) + '...';

    const handleSelect = () => {
        onSelect(id);
    };

    const handleIncreaseCount = () => {
        if (count + 1 < 999) {
            setCount(count + 1);
            onIncreaseCount(id);
        }
    };

    const handleDecreaseCount = () => {
        if (count - 1 > 0) {
            setCount(count - 1);
            onDecreaseCount(id);
        }
    };

    const handleDelete = () => {
        onDelete(id);
    };

    return (
        <div className={ `${styles.cardContainer}` }>
            <div className={ `${styles.cardBorder}` }></div>
            <div className={ `${styles.cardWrapper}` }>
                <div>
                    <Image 
                    className={ `${styles.cardImage}` }
                    src='/images/product.jpg'
                    alt='Product'
                    width={90}
                    height={120}
                    />
                </div>
                <div className={ `${styles.cardInner}` }>
                    <div className={ `${styles.cardInnerLine}` }>
                        <p className={ `${styles.cardName}` }>{title}</p>
                        <div className={ `${styles.cardCounterContainer}` }>
                            <button className={ `${styles.increase}` } onClick={handleIncreaseCount}></button>
                            <input className={ `${styles.counter}` } type="number" value={count} readOnly={true} min={1} max={999}/>
                            <button className={ `${styles.decrease}` } onClick={handleDecreaseCount}></button>
                        </div>
                    </div>
                    <div className={ `${styles.cardInnerLine}` }>
                        <p className={ `${styles.price}` }>{getFormattedPrice(price)} ₽</p>
                        <div className={ `${styles.selectionContainer}` }>
                            <DeleteSVG className={ `${styles.delete}` } onClick={handleDelete}></DeleteSVG>
                            <input className={ `${styles.select}` } type="checkbox" onChange={handleSelect} checked={selected || false}/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ProductCardCart;