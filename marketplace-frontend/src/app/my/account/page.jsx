'use client'

import CustomButton from '@/components/CustomButton/CustomButton';
import styles from './page.module.css'
import ValidateInput from '@/components/ValidateInput/ValidateInput';
import { logout } from '@/utils/requests';
import { redirect, useRouter } from 'next/navigation';
import { useAuth } from '@/hooks/AuthContext';

// const validationObj = {
//         NAME: [
//             {
//                 message: 'Минимальная длина 10 символов',
//                 regExp: /[A-Za-zА-Яа-яЁё\s]{10,}/
//             },
//             {
//                 message: 'Не должно быть цифр',
//                 regExp: /[^\d]+/
//             },
//             {
//                 message: 'Максимальная длина 7 символов',
//                 regExp: /^.{0,7}$/
//             }
//         ]
//     };

const Profile = () => {
    const {setIsAuthenticated} = useAuth();
    const router = useRouter();

    const handleSave = () => {

    };

    const handleLogout = () => {
        logout(redirect, setIsAuthenticated);
    };

    return(
        <>
            <h1 className={ `${styles.pageHeader}` }>Личный кабинет покупателя</h1>
            <div className={ `${styles.pageContentContainer}` }>
                <div className={ `${styles.navPanel}` }>
                    <div className={ `${styles.navWrapper}` }>
                        <div className={ `${styles.navPanelInfo}` }>
                            <div className={ `${styles.avatar}` }></div>
                            <p className={ `${styles.navPanelName}` }>Иван Иванов</p>
                        </div>
                        <div className={ `${styles.navListContainer}` }>
                            <nav>
                                <ul className={ `${styles.navList}` }>
                                    <li className={ `${styles.navListItem}` }><a href='#' className={ `${styles.navListItemLink} ${styles.navListItemActive}` }>Личная информация</a></li>
                                    <li className={ `${styles.navListItem}` }><a href='#' className={ `${styles.navListItemLink}` }>Стать продавцом</a></li>
                                    <li className={ `${styles.navListItem}` }><a href='#' className={ `${styles.navListItemLink}` }>Отзывы</a></li>
                                </ul>
                            </nav>
                        </div>
                    </div>
                    <button className={ `${styles.logoutButton}` } onClick={handleLogout}>Выйти</button>
                </div>
                <div className={ `${styles.currentContentContainer}` }>
                    <div className={ `${styles.labelsList}` }>
                        <label htmlFor='name' className={ `${styles.labelWrapper}` }>
                            <p className={ `${styles.labelTitle}` }>ФИО</p>
                            <ValidateInput
                            errorClassName={styles.errorInput}
                            // validationArray={validationObj.NAME}
                            >
                                <input id='name' className={ `${styles.input}` }/>
                            </ValidateInput>
                        </label>
                        <label htmlFor='phone' className={ `${styles.labelWrapper}` }>
                            <p className={ `${styles.labelTitle}` }>Телефон</p>
                            <ValidateInput
                            errorClassName={styles.errorInput}
                            >
                                <input id='phone' className={ `${styles.input}` }/>
                            </ValidateInput>
                        </label>
                        <label htmlFor='email' className={ `${styles.labelWrapper}` }>
                            <p className={ `${styles.labelTitle}` }>Почта</p>
                            <ValidateInput
                            errorClassName={styles.errorInput}
                            >
                                <input id='email' className={ `${styles.input}` }/>
                            </ValidateInput>
                        </label>
                        <label htmlFor='birth-date' className={ `${styles.labelWrapper}` }>
                            <p className={ `${styles.labelTitle}` }>Дата рождения</p>
                            <ValidateInput
                            errorClassName={styles.errorInput}
                            >
                                <input id='birth-date' className={ `${styles.input}` }/>
                            </ValidateInput>
                        </label>
                        <label htmlFor='sex' className={ `${styles.labelWrapper}` }>
                            <p className={ `${styles.labelTitle}` }>Пол</p>
                            <ValidateInput
                            errorClassName={styles.errorInput}
                            >
                                <input id='sex' className={ `${styles.input}` }/>
                            </ValidateInput>
                        </label>
                    </div>
                    <div className={ `${styles.saveContainer}` }>
                        <p className={ `${styles.message} ${styles.success}` }>Изменения сохранены.</p>
                        <CustomButton className={ `${styles.saveButton}` } onClick={handleSave}>Сохранить</CustomButton>
                    </div>
                </div>
            </div>
        </>
    );
};

export default Profile;