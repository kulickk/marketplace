import styles from './CustomButton.module.css';
import React from 'react';

const CustomButton = ({children, onClick, className}) => {
    const isTextOnly = React.Children.toArray(children).every(child => {
        return typeof child === 'string' || typeof child === 'number';
    });

    if (!isTextOnly) throw new Error('Children prop is not TextOnly');

    return(
        <button 
        onClick={onClick} 
        className={ `${styles.customButton} ${className}` }>
            <span 
            className={ `${styles.buttonText}` }>
                {children}
            </span>
        </button>
    );
};

export default CustomButton;