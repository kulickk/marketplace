import dayjs from "dayjs";
import { ORDER_DATE_FORMAT } from "./const";

const addDommenToApi = (apiObj, dommen) => {
    const processed = {};

    for (const key in apiObj) {
        const value = apiObj[key];

        if (typeof value === 'string') {
            processed[key] = dommen + value;
        } else if (typeof value === 'function') {
            processed[key] = (...args) => dommen + value(...args);
        } else if (typeof value === 'object' && value !== null) {
            processed[key] = addDommenToApi(value, dommen);
        } else {
            processed[key] = value;
        }
    }

    return processed;
};

const getFormattedPrice = (price) => {
    return price.toString().split('').reverse().join('').match(/.{1,3}/g).join(' ').split('').reverse().join('');
};

const getRandomInt = (max) => {
    return Math.floor(Math.random() * max);
};

const getOrderDate = (date) => dayjs(date).format(ORDER_DATE_FORMAT);

const getGoodsCount = (count) => {
  const lastDigit = count % 10;
  const lastTwoDigits = count % 100;
  
  if (lastTwoDigits >= 11 && lastTwoDigits <= 19) {
    return `${count} товаров`;
  }
  
  switch (lastDigit) {
    case 1:
      return `${count} товар`;
    case 2:
    case 3:
    case 4:
      return `${count} товара`;
    default:
      return `${count} товаров`;
  }
};

export {addDommenToApi, getFormattedPrice, getRandomInt, getOrderDate, getGoodsCount};