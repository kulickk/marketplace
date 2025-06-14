'use client'
import { useParams } from "next/navigation";
import PaymentWidget from "../components/PaymentWidget";

const PaymentPage = () => {
    const { paymentToken } = useParams();

    return(
        <>
        <PaymentWidget 
        paymentToken={paymentToken}
        />
        </>
    );
};

export default PaymentPage;