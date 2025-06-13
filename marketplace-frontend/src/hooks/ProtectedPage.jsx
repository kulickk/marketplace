'use client';

import { frontendRouter, marketApi } from '@/utils/const';
import { useAuth } from '../contexts/AuthContext';
import { redirect } from 'next/navigation';

const ProtectedPage = ({ children }) => {
    const {isAuthenticated} = useAuth();

    if (isAuthenticated === null) <></>;

    if (isAuthenticated === false) redirect(frontendRouter.INDEX);
    
    
    return <>{children}</>;
}

export default ProtectedPage;