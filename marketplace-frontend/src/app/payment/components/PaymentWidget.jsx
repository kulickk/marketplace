import { useEffect, useState } from 'react';
import Script from 'next/script';
import { PAYMENT_RETURN_URL } from '@/utils/const';

const PaymentWidget = ({ paymentToken, onSuccess }) => {
  const [isScriptLoaded, setIsScriptLoaded] = useState(false);

  const initializeWidget = () => {
    if (!window.YooMoneyCheckoutWidget) return;

    const checkout = new window.YooMoneyCheckoutWidget({
      confirmation_token: paymentToken, // Получаете с бэкенда
      return_url: PAYMENT_RETURN_URL,
      error_callback: function(error) {
        console.error('Payment error:', error);
      },
    });

    checkout.render('payment-form');
    
    checkout.on('success', (event) => {
      onSuccess(event);
    });
  };

  // Инициализируем виджет после загрузки скрипта
  useEffect(() => {
    if (isScriptLoaded) {
      initializeWidget();
    }
  }, [isScriptLoaded]);

  return (
    <>
      <Script
        src="https://yookassa.ru/checkout-widget/v1/checkout-widget.js"
        strategy="afterInteractive"
        onLoad={() => setIsScriptLoaded(true)}
      />
      <div id="payment-form" />
    </>
  );
};

export default PaymentWidget;