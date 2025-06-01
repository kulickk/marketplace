import styles from "./page.module.css";
import ProductCard from "@/components/ProductCard/ProductCard";

const Home = () => {
  return (
    <div className={styles.page}>
      <ProductCard />
      <ProductCard />
      <ProductCard />
      <ProductCard />
      <ProductCard />
      <ProductCard />
      <ProductCard />
      <ProductCard />
      <ProductCard />
      <ProductCard />
    </div>
  );
}

export default Home;