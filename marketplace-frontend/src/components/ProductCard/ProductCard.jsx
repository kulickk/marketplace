import Link from 'next/link';
import styles from './ProductCard.module.css'
import { marketApi, frontendRouter } from '@/utils/const';
import { getFormattedPrice } from '@/utils/utils';


const ProductCard = ({id, name, price, images}) => {
    const title = (name.length > 34) ? name.slice(0, 34) + '...' : name;
    const newPrice = Math.floor(price);
    return (
        <Link 
        className={ `${styles.cardContainer}` }
        href={frontendRouter.PRODUCT(id)}
        >
            <div className={ `${styles.cardBorder}` }></div>
            <div className={ `${styles.cardWrapper}` }>
                <div className={ `${styles.cardImageContainer}` }>
                    { (images && images[0]) ? <img className={ `${styles.cardImage}` } src={ marketApi.GET_GOOD_PHOTO(images[0]) } /> : 
                    <div className={ `${styles.cardImage} ${styles.goodImageNone}` }></div>
                    }
                    {/* <img className={ `${styles.cardImage}` } src={ marketApi.GET_GOOD_PHOTO(images[0]) } width={150} height={200}/> */}
                </div>
                <p className={ `${styles.price}` }>{getFormattedPrice(newPrice)} ₽</p>
                <p className={ `${styles.title}` }>{ title }</p>
            </div>
        </Link>
    );
};

export default ProductCard;