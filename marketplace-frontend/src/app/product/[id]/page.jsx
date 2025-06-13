'use client'
import CustomButton from '@/components/CustomButton/CustomButton';
import styles from './page.module.css';
import Image from 'next/image';
import { redirect, useParams } from 'next/navigation';
import { frontendRouter, marketApi } from '@/utils/const';
import { useEffect, useState } from 'react';
import { addToCart, getProductInfo } from '@/utils/requests';
import { useAuth } from '@/contexts/AuthContext';
import { getFormattedPrice } from '@/utils/utils';

const getGoodGallery = (imagePaths, pigPicture) => {
    return imagePaths.map((imagePath, index) => {
        return(
            <li key={index} className={ `${styles.galleryItem}` } data-active={pigPicture === imagePath}>
                <img 
                className={ `${styles.goodImage} ${styles.goodImageGalleryItem}` }
                src={marketApi.GET_GOOD_PHOTO(imagePath)}
                alt='Product'
                data-image-path={imagePath}
                />
            </li>
        );
    });
};

const ProductPage = () => {
    const { id } = useParams();
    const [imageUrls, setImageUrls] = useState('');
    const [bigPicture, setBigPicture] = useState(null);
    const [product, setProduct] = useState(null);
    const {setCartCount, isAuthenticated} = useAuth();

    const handleAddToCart = (evt) => {
        evt.preventDefault();
        addToCart({id, redirect, setCartCount});
    };

    const handleGalleryItemClick = (evt) => {
        if (evt.target.tagName === 'LI') {
            setBigPicture(evt.target.querySelector('img').dataset.imagePath);
        }
    };

    useEffect(() => {
        getProductInfo({id, setImageUrls, setProduct, isAuthenticated, setBigPicture});
    }, []);

    if (product !== null) {
        const {name, price, description, isInCart} = product;
        const newPrice = Math.floor(price);
        return(
            <>
                <div className={ `${styles.productPage}` }>
                    <div className={ `${styles.goodContainer}` }>
                        <div className={ `${styles.galleryContainer}` }>
                            <div className={ `${styles.galleryListContainer}` }>
                                <ul className={ `${styles.galleryList}` } onClick={handleGalleryItemClick}>
                                    { imageUrls.length > 0 ? getGoodGallery(imageUrls, bigPicture) : ''}
                                </ul>
                            </div>
                            {(bigPicture) ? <img className={ `${styles.goodImage} ${styles.activeGoodImage}` } src={ marketApi.GET_GOOD_PHOTO(bigPicture) } width={430} height={573}/> : 
                            <div className={ `${styles.goodImage} ${styles.activeGoodImage} ${styles.activeGoodImageNone}` }></div>
                            }
                        </div>
                        {/* <div className={ `${styles.goodInfoContainer}` }> */}
                            <div className={ `${styles.goodDescriptionContainer} ${styles.goodInfoItem}` }>
                                <p className={ `${styles.goodTitle}` }>{name}</p>
                                <div>В наличии: {product.stock}</div>
                                <div className={ `${styles.goodRateContainer}` }>
                                    <p>4.9</p>
                                    <p>6241 отзыв</p>
                                </div>
                                { (description) ? (<>
                                <p>О товаре</p>
                                <p>{description}</p></>) : (<></>) }
                            </div>
                            <div className={ `${styles.toCartContainer} ${styles.goodInfoItem}` }>
                                <p className={ `${styles.goodPrice}` }>{getFormattedPrice(newPrice)} ₽</p>
                                { (!isInCart) ? (<CustomButton className={ `${styles.toCartButton}` } onClick={handleAddToCart} disabled={!isAuthenticated}>В корзину</CustomButton>) : 
                                (<CustomButton className={ `${styles.toCartButton}` } onClick={() => redirect(frontendRouter.CART)} >Уже в корзине</CustomButton>)}
                            </div>
                        {/* </div> */}
                    </div>
                    <p className={ `${styles.rewievTitle}` }>Отзывы</p>
                    <p>Отзывы отсутствуют</p>
                </div>
            </>
        );
    }
};

export default ProductPage;