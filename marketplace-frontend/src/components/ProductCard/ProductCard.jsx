import Link from 'next/link';
import styles from './ProductCard.module.css'
import Image from 'next/image';

const desc = 'Кроссовки Adidas Sportswear CRAZYCasdasdasdasd asdasdasd asdasdas dasdasdasaSD DASDASDASSD';

const ProductCard = () => {
    const title = desc.slice(0, 34) + '...';
    return (
        <Link 
        className={ `${styles.cardContainer}` }
        href={'/product/1'}
        >
            <div className={ `${styles.cardBorder}` }></div>
            <div className={ `${styles.cardWrapper}` }>
                <div className={ `${styles.cardImageContainer}` }>
                    <Image 
                    className={ `${styles.cardImage}` }
                    src='/images/product.jpg'
                    alt='Product'
                    width={150}
                    height={200}
                    />
                </div>
                <p className={ `${styles.price}` }>1 777 ₽</p>
                <p className={ `${styles.title}` }>{ title }</p>
            </div>
        </Link>
    );
};

export default ProductCard;