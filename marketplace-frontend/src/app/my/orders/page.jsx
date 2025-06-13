'use client'
import styles from './page.module.css'
import { useEffect, useState } from 'react';
import { getFormattedPrice, getRandomInt } from '@/utils/utils';
import ProductCardCart from '@/components/ProductCard/ProductCardInCart/ProductCardInCart';
import { getOrders } from '@/utils/requests';
import OrdersCard from '@/components/ProductCard/OrdersCard/OrdersCard';


const getOrdersTemplate = (orders) => {
    console.log(orders);
    return orders.map((order) => {
        return(
            <OrdersCard
            key={order.orderId}
            id={order.orderId}
            price={order.totalAmount}
            createdAt={order.createdAt}
            items={order.items}
            />
        );
    });
};

const Orders = () => {
    const [orders, setOrders] = useState([]);

    useEffect(() => {
        getOrders({setOrders});
    }, [])

    if (orders.length > 0) {
        console.log(orders);
        return(
        <>
            <h1 className={`${styles.pageHeader}`}>Заказы</h1>
            <div className={`${styles.pageContentContainer}`}>
                {getOrdersTemplate(orders)}
            </div>
        </>
    );
    }
    return(
        <>
            <h1 className={`${styles.pageHeader}`}>Заказы</h1>
            <div className={`${styles.pageContentContainer}`}>
                Заказы отсутствуют
            </div>
        </>
    );
};

export default Orders;