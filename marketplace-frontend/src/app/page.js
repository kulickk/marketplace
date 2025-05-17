import styles from "./page.module.css";
import ProductCard from "@/components/ProductCard/ProductCard";

export default function Home() {
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
