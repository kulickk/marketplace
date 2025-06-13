'use client'
import { useEffect, useState } from "react";
import styles from "./page.module.css";
import ProductCard from "@/components/ProductCard/ProductCard";
import { getGoods } from "@/utils/requests";

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

const Home = () => {
  const [goods, setGoods] = useState([]);

  useEffect(() => {
    getGoods({goods, setGoods});
  }, []);

  if (goods) {
    return (
        <div className={styles.page}>
          {getCards(goods)}
        </div>
    );
  }
}

export default Home;