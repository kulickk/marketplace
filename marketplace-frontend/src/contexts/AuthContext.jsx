"use client"
import { getUserInfo } from '@/utils/requests';
import { createContext, useState, useContext, useEffect } from 'react';

const AuthContext = createContext();

const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(null);
  const [userRole, setUserRole] = useState(null);
  const [ordersCount, setOrdersCount] = useState(null);
  const [cartCount, setCartCount] = useState(null);
  const [userName, setUserName] = useState(null);

  useEffect(() => {
    getUserInfo({setIsAuthenticated, setUserRole, setCartCount, setOrdersCount, setUserName});
  }, []);

  return (
    <AuthContext.Provider 
      value={{
        isAuthenticated, 
        setIsAuthenticated,
        userRole, 
        setUserRole,
        ordersCount, 
        setOrdersCount,
        cartCount, 
        setCartCount,
        userName, 
        setUserName
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

const useAuth = () => useContext(AuthContext);

export {AuthProvider, useAuth};