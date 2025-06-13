import styles from './layout.module.css'

const ProductLayout = ({ children }) => {
  return (
    <div className={ `${styles.pageContainer}` }>
        { children }
    </div>
  );
}

export default ProductLayout;