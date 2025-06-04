'use client';

import { frontendRouter, marketApi } from '@/utils/const';
import { useAuth } from './AuthContext';
import { redirect } from 'next/navigation';

const ProtectedPage = ({ children }) => {
    const {isAuthenticated} = useAuth();

    if (!isAuthenticated) redirect(frontendRouter.INDEX);
    
    return <>{children}</>;
}

export default ProtectedPage;