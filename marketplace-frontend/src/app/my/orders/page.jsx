'use client'
import styles from './page.module.css'
import { getOrders } from '@/utils/requests';
import OrdersCard from '@/components/ProductCard/OrdersCard/OrdersCard';
import { useEffect, useState } from 'react';


const getOrdersTemplate = (orders) => {
    return orders.map((order) => {
        console.log(order.orderPaymentStatus);
        return(
            <OrdersCard
            key={order.orderId}
            id={order.orderId}
            price={order.totalAmount}
            createdAt={order.createdAt}
            paymentStatus={order.orderPaymentStatus}
            paymentMessage={order.paymentCancelReason}
            paymentToken={order.paymentUrl}
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

    if (orders) {
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