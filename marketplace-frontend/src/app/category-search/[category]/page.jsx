'use client'
import { useEffect, useState } from "react";
import styles from "./page.module.css";
import ProductCard from "@/components/ProductCard/ProductCard";
import { getGoodsByCategory } from "@/utils/requests";
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

const SearchCategory = () => {
  const [goods, setGoods] = useState([]);
  const [categoryName, setCategoryName] = useState(null);
  const {category} = useParams();

  useEffect(() => {
    console.log(category);
    getGoodsByCategory({categoryId: category, setGoods, setCategoryName});
  }, []);

  if (goods.length > 0) {
    return (
        <>
        <h1 className={styles.title}>Товары в категории: "{categoryName}"</h1>
        <div className={styles.page}>
          {getCards(goods)}
        </div>
        </>
    );
  } else {
    return (
        <>
        <h1 className={styles.title}>Товары в категории: "{categoryName}"</h1>
        <div className={styles.page}>
          Товары отсутствуют
        </div>
        </>
    );
  }
}

export default SearchCategory;