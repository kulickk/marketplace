import './globals.css'
import Header from "@/components/Header/Header";
import styles from './layout.module.css'
import localFont from 'next/font/local';
import { AuthProvider } from '@/contexts/AuthContext';

const geologica = localFont({
  src: [
    {
      path: '../../public/fonts/Geologica-Regular.woff2',
      weight: '400',
      style: 'normal',
    },
    {
      path: '../../public/fonts/Geologica-Bold.woff2',
      weight: '700',
      style: 'normal',
    },
  ],
  display: 'swap',
  variable: '--font-geologica',
});

export const metadata = {
  viewport: {
    width: 'device-width',
    initialScale: 1.0,
  },
};

export default function RootLayout({ children }) {
  return (
    <html lang="ru" className={ geologica.className }>
      <body className={ styles.mainContainer }>
        <AuthProvider>
          <Header />
          {children}
        </AuthProvider>
      </body>
    </html>
  );
}
