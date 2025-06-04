import CustomButton from '@/components/CustomButton/CustomButton';
import styles from './page.module.css';
import Image from 'next/image';

const ProductPage = () => {
    return(
        <>
            <div className={ `${styles.productPage}` }>
                <div className={ `${styles.goodContainer}` }>
                    <div className={ `${styles.galleryContainer}` }>
                        <div className={ `${styles.galleryListContainer}` }>
                            <ul className={ `${styles.galleryList}` }>
                                <li className={ `${styles.galleryItem}` }>
                                    <Image
                                    className={ `${styles.goodImage}` }
                                    src='/images/product.jpg'
                                    alt='Product'
                                    width={100}
                                    height={133}
                                    />
                                </li>
                                <li className={ `${styles.galleryItem}` }>
                                    <Image
                                    className={ `${styles.goodImage}` }
                                    src='/images/product.jpg'
                                    alt='Product'
                                    width={100}
                                    height={133}
                                    />
                                </li>
                                <li className={ `${styles.galleryItem}` }>
                                    <Image
                                    className={ `${styles.goodImage}` }
                                    src='/images/product.jpg'
                                    alt='Product'
                                    width={100}
                                    height={133}
                                    />
                                </li>
                                <li className={ `${styles.galleryItem}` }>
                                    <Image
                                    className={ `${styles.goodImage}` }
                                    src='/images/product.jpg'
                                    alt='Product'
                                    width={100}
                                    height={133}
                                    />
                                </li>
                            </ul>
                        </div>
                        <Image
                        className={ `${styles.goodImage} ${styles.activeGoodImage}` }
                        src='/images/product.jpg'
                        alt='Product'
                        width={430}
                        height={573}
                        />
                    </div>
                    {/* <div className={ `${styles.goodInfoContainer}` }> */}
                        <div className={ `${styles.goodDescriptionContainer} ${styles.goodInfoItem}` }>
                            <p className={ `${styles.goodTitle}` }>Кроссовки Adidas Sportswear CRAZYCHAOS 2000</p>
                            <div className={ `${styles.goodRateContainer}` }>
                                <p>4.9</p>
                                <p>6241 отзыв</p>
                            </div>
                            <p>О товаре</p>
                            <p>Материал: Синтетика<br/>
                                Материал стельки: Текстиль<br/>
                                Материал подошвы: Полимерный материал<br/>
                                Размер: 39
                            </p>
                        </div>
                        <div className={ `${styles.toCartContainer} ${styles.goodInfoItem}` }>
                            <p className={ `${styles.goodPrice}` }>1 777 ₽</p>
                            {/* <button className={ `${styles.toCartButton}` }>В корзину</button> */}
                            <CustomButton className={ `${styles.toCartButton}` }>В корзину</CustomButton>
                        </div>
                    {/* </div> */}
                </div>
                <p className={ `${styles.rewievTitle}` }>Отзывы</p>
            </div>
        </>
    );
};

export default ProductPage;