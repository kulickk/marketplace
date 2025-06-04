"use client"
import { marketApi } from '@/utils/const';
import { createContext, useState, useContext, useEffect } from 'react';

const AuthContext = createContext();

const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    fetch(marketApi.AUTH.CHECK, {
      credentials: 'include'
    }).then(response => {
      if (response.ok) {
        setIsAuthenticated(true);
      }
    })
  }, []);

  return (
    <AuthContext.Provider 
      value={{
        isAuthenticated, 
        setIsAuthenticated 
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

const useAuth = () => useContext(AuthContext);

export {AuthProvider, useAuth};