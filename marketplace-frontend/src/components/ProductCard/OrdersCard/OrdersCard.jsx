import { getFormattedPrice, getGoodsCount, getOrderDate } from "@/utils/utils";
import styles from './OrdersCard.module.css';
import { marketApi } from "@/utils/const";

const getGoods = (goods) => {
    const slicedGoods = goods.slice(0, 4);
    return slicedGoods.map(good => {
        return(
            <li key={good.id}><img className={ `${styles.goodPhoto}` } src={marketApi.GET_GOOD_PHOTO(good.imagePath)} alt="Фото товара"/></li>
        );
    });
};

const OrdersCard = ({price, createdAt, items}) => {
    const newPrice = Math.floor(price);
    const goodsCount = items.length;
    return(
        <div className={ `${styles.orderContainer}` }>
            <div className={ `${styles.orderBorder}` }></div>
            <div className={ `${styles.orderWrapper}` }>
                <div className={ `${styles.orderInfoContainer}` }>
                    <div className={ `${styles.orderInfoHeader}` }>
                        <div className={ `${styles.orderInfoHeaderContainer}` }>
                            <p className={ `${styles.orderDate}` }>Заказ от {getOrderDate(createdAt)}</p>
                            <p className={ `${styles.orderNumber}` }>номер заказа</p>
                        </div>
                        <div className={ `${styles.orderStatus}` }>
                            <p className={ `${styles.orderStatusName}` }>Ожидает оплаты</p>
                        </div>
                    </div>
                    <div className={ `${styles.orderInfoFooter}` }>
                        <p className={ `${styles.orderPrice}` }>{getFormattedPrice(newPrice)} ₽</p>
                        <svg className={ `${styles.orderInfoFooterSpliter}` } width="3" height="4" viewBox="0 0 3 4" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M1.50356 3.276C1.16223 3.276 0.866229 3.16667 0.615563 2.948C0.370229 2.72933 0.247563 2.444 0.247563 2.092C0.247563 1.852 0.303563 1.644 0.415563 1.468C0.527563 1.28667 0.679563 1.148 0.871563 1.052C1.06356 0.950666 1.27423 0.9 1.50356 0.9C1.85023 0.9 2.14356 1.00933 2.38356 1.228C2.6289 1.44133 2.75156 1.72933 2.75156 2.092C2.75156 2.32667 2.69556 2.53467 2.58356 2.716C2.47156 2.892 2.31956 3.03067 2.12756 3.132C1.9409 3.228 1.7329 3.276 1.50356 3.276Z" fill="black"/>
                        </svg>
                        <p className={ `${styles.orderGoodsCount}` }>{getGoodsCount(goodsCount)}</p>
                    </div>
                </div>
                <div className={ `${styles.goodsContainer}` }>
                    <ul className={ `${styles.goodsList}` }>
                        {getGoods(items)}
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default OrdersCard;