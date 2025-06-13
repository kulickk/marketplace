'use client'
import { useEffect, useState } from "react";
import styles from "./page.module.css";
import ProductCard from "@/components/ProductCard/ProductCard";
import { getGoods, getGoodsByName } from "@/utils/requests";
import { useParams } from "next/navigation";

const getCards = (goods) => {
  return goods.map((good, index) => {
    return(
      <ProductCard
      key={index}
      id={good.id}
      name={good.name}
      price={good.price}
      images={good.imagePaths}
      />
    );
  });
};

const SearchProductName = () => {
  const [goods, setGoods] = useState([]);
  const {product} = useParams();

  useEffect(() => {
    getGoodsByName({goodName: product, setGoods});
  }, []);

  if (goods.length > 0) {
    return (
        <>
        <h1 className={styles.title}>Поиск товара: "{decodeURIComponent(product)}"</h1>
        <div className={styles.page}>
          {getCards(goods)}
        </div>
        </>
    );
  } else {
    return (
        <>
        <h1 className={styles.title}>Поиск товара: "{decodeURIComponent(product)}"</h1>
        <div className={styles.page}>
          Товары отсутствуют
        </div>
        </>
    );
  }
}

export default SearchProductName;