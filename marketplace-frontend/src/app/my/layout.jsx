import ProtectedPage from '@/hooks/ProtectedPage';
import styles from './layout.module.css'

const AccountLayout = ({ children }) => {
  return (
    <ProtectedPage>
      <div className={ `${styles.pageContainer}` }>
        { children }
      </div>
    </ProtectedPage>
  );
}

export default AccountLayout;