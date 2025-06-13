'use client'
import React, { useState } from 'react';
import styles from './ValidateInput.module.css'

// const Messages = ({messages}) => {
//     // console.log(messages);
//     return messages.map((message, index) => {
//         if (message.length > 0 ) {
//             return(
//                 <p key={index} className={ `${styles.message}` }>{message}</p>
//             );
//         }
//     }).join('');
// };

const ValidateInput = ({children, errorClassName, validationArray}) => {
    const [isValid, setIsValid] = useState(true);
    const [inputValue, setInputValue] = useState('');
    const [messages, setMessages] = useState([]);

    const validate = (value) => {
        let messagesCount = 0;
        const messagesArray = validationArray.map((valid, index) => {
            console.log(valid.regExp, !valid.regExp.test(value), value);
            if (!valid.regExp.test(value)) {
                messagesCount += 1
                return (<p key={index} className={ `${styles.message}` }>{valid.message}</p>);
            }
            return '';
        });
        console.log(messagesCount, messagesArray);
        if (messagesCount > 0) {
            setIsValid(false);
            setMessages(messagesArray);
        } else {
            setIsValid(true);
            setMessages([]);
        }
    };

    const onValueChange = (evt) => {
        setInputValue(evt.target.value);
        validate(evt.target.value);
    };

    const enchancedInput = () => {
        return React.Children.map(children, (child) => {
            if (React.isValidElement(child) && child.type === 'input') {
                return React.cloneElement(child, {
                    className: `${child.props.className || ''} ${(!isValid) ? errorClassName : ''}`,
                    onChange: onValueChange
                });
            }
            return child;
        });
    };

    const a = enchancedInput();

    return(
        <div className={ `${styles.validationContainer}` }>
            {a}
            <div className={ `${styles.messagesContainer}` }>
                {/* <p className={ `${styles.message}` }>Минимальная длина 10 символов</p>
                <p className={ `${styles.message}` }>Максимальная длина 60 символов</p>
                <p className={ `${styles.message}` }>Недопустимые символы (&%#)</p> */}
                {/* {Messages({messages})} */}
                {/* <Messages messages={messages}/> */}
                {messages}
            </div>
        </div>
    );
};

export default ValidateInput;


// validation - массив объектов (сообщение + регулярка для проверки)
// validation = [
//     {
//         message: 'Минимальная длина 10 символов',
//         regExp: /[A-Za-zА-Яа-яЁё]{10,}/
//     },
//     {
//         message: 'Максимальная длина 60 символов',
//         regExp: /[A-Za-zА-Яа-яЁё]{,60}/
//     },
//     {
//         message: 'Недопустимые символы (&%#)',
//         regExp: /[A-Za-zА-Яа-яЁё]+/
//     }
// ];