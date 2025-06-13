import DeleteSVG from '@/svg/DeleteSVG';
import styles from './ProductCardInCart.module.css'
import { getFormattedPrice } from '@/utils/utils';
import { useState } from 'react';
import { marketApi } from '@/utils/const';

const ProductCardCart = ({id, goodId, desc, price, quantity = 1, imagePath, onSelect, onIncreaseCount, onDecreaseCount, onDelete, selected}) => {
    const [count, setCount] = useState(quantity);
    const title = (desc.length < 34) ? desc : desc.slice(0, 34) + '...';
    const newPrice = Math.floor(price);

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
        onDelete({id, goodId});
    };

    return (
        <div className={ `${styles.cardContainer}` } key={id}>
            <div className={ `${styles.cardBorder}` }></div>
            <div className={ `${styles.cardWrapper}` }>
                <div>
                    { (imagePath) ? 
                    <img className={ `${styles.cardImage}` } src={ marketApi.GET_GOOD_PHOTO(imagePath) }/>
                    : <div className={ `${styles.cardImage} ${styles.goodImageNone}` }></div>}
                </div>
                <div className={ `${styles.cardInner}` }>
                    <div className={ `${styles.cardInnerLine}` }>
                        <p className={ `${styles.cardName}` }>{title}</p>
                        <div className={ `${styles.cardCounterContainer}` }>
                            <button className={ `${styles.decrease}` } onClick={handleDecreaseCount}></button>
                            <input className={ `${styles.counter}` } type="number" value={count} readOnly={true} min={1} max={999}/>
                            <button className={ `${styles.increase}` } onClick={handleIncreaseCount}></button>
                        </div>
                    </div>
                    <div className={ `${styles.cardInnerLine}` }>
                        <p className={ `${styles.price}` }>{getFormattedPrice(newPrice)} ₽</p>
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