import ProtectedPage from '@/hooks/ProtectedPage';
import styles from './layout.module.css'

const AccountLayout = ({ children }) => {
  return (
    <div className={ `${styles.pageContainer}` }>
      { children }
    </div>
  );
}

export default AccountLayout;