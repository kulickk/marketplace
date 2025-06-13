'use client'

import CustomButton from '@/components/CustomButton/CustomButton';
import styles from './page.module.css'
import ValidateInput from '@/components/ValidateInput/ValidateInput';
import { createGood, getCategories, getMyGoods, getUsers, logout, promoteSeller } from '@/utils/requests';
import { redirect, useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';
import { ACCOUNT_CONTENT, USER_ROLE, USER_ROLE_NAME } from '@/utils/const';
import { useEffect, useRef, useState } from 'react';
import { getFormattedPrice } from '@/utils/utils';

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
const getCategoriestemplate = (categories) => {
    return categories.map(category => {
        return(
            <option key={category.id} value={category.id}>{category.name}</option>
        );
    });
};

const getMyGoodsTemplate = (myGoods) => {
    return myGoods.map(myGood => {
        const newPrice = Math.floor(myGood.price);
        return(
            <li key={myGood.id} className={ `${styles.myGoodItem}` }>
                <div>{myGood.name}</div>
                <div className={ `${styles.myGoodPrice}` }>{getFormattedPrice(newPrice)} ₽</div>
            </li>
        );
    });
};

const getUsersTemplate = (users, seletedUser) => {
    return users.map(user => {
        return(
            <tr key={user.id} className={ `${styles.tableContainer} ${styles.tableRow} ${ (seletedUser && (seletedUser.userId === user.id)) ? styles.tableRowActive : ''}`} id={user.id} data-role={user.role}>
                <td>{user.lastName} {user.firstName}</td>
                <td className={ `${styles.myGoodPrice}` }>{user.role}</td>
            </tr>
        );
    });
};

const getNewGoodPhotos = (photos) => {
    return Array.from(photos).map((photo, index) => {
        return(
            <img key={index} src={URL.createObjectURL(photo)} alt="Превью фото" />
        );
    });
};

const getCurrentContent = (currentContent, props) => {
    const [curentPhotos, setCurentPhotos] = useState(null);

    switch(currentContent) {
        case ACCOUNT_CONTENT.PRESONAL_INFORMATION:
            const {handleSave} = props;
            return(
                <>
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
                </>
            );
        case ACCOUNT_CONTENT.REVIEWS:
            return(
                <>
                ОТЗЫВЫ
                </>
            );
        case ACCOUNT_CONTENT.MY_GOODS:
            const {myGoods, isCreateGoodFormOpen, setIsCreateGoodFormOpen, goodName, goodPhoto, goodPrice, goodQuantity, goodCategory, goodDescription, categories} = props;

            const hadlecreateGoodFormOpen = () => {
                setIsCreateGoodFormOpen(true);
            };

            const handleCreateGood = () => {
                const good ={
                    name: goodName.current.value,
                    description: goodDescription.current.value,
                    price: goodPrice.current.value,
                    quantity: goodQuantity.current.value,
                    categoryId: goodCategory.current.value
                }

                createGood({good, images: curentPhotos});
            };

            const clearCreateGoodForm = () => {
                setCurentPhotos(null);
            };

            const hadlecreateGoodFormClose = () => {
                console.log(goodPhoto.current.files);
                setIsCreateGoodFormOpen(false);
                clearCreateGoodForm();
            };

            const onFilesSelect = (evt) => {
                if (evt.target.files.length > 4) {
                    const dataTransfer = new DataTransfer();
                    for (let i = 0; i < 4; i++) {
                        dataTransfer.items.add(evt.target.files[i]);
                    }
                    evt.target.files = dataTransfer.files;
                    setCurentPhotos(dataTransfer.files);
                    return;
                }
                setCurentPhotos(evt.target.files);
            };

            if (isCreateGoodFormOpen) {
                return(
                    <>
                    <div className={ `${styles.goodCreateContainer}`}>
                        <div>
                            <p>Название товара</p>
                            <input type='text' ref={goodName}/>
                        </div>
                        <div className={ `${styles.newGoodImagesSelectContainer}`}>
                            <div className={ `${styles.newGoodImagesSelectInner}`}>
                                <p>Фото товара</p>
                                <input className={ `${styles.imagesInput}`} type='file' ref={goodPhoto} multiple onChange={onFilesSelect}/>
                                <CustomButton className={ `${styles.imagesInputLabel}`} onClick={() => goodPhoto.current.click()}>Загрузить</CustomButton>
                            </div>
                            { curentPhotos ? 
                            <div className={ `${styles.newGoodPreviewImagesContainer}`}>{getNewGoodPhotos(curentPhotos)}</div> : ''}
                        </div>
                        <div>
                            <p>Цена (в рублях)</p>
                            <input type='text' ref={goodPrice}/>
                        </div>
                        <div>
                            <p>Количество товара</p>
                            <input type='text' ref={goodQuantity}/>
                        </div>
                        <div>
                            <p>Категория</p>
                            <select ref={goodCategory}>
                                { categories ? getCategoriestemplate(categories) : ''}
                            </select>
                        </div>
                        <div>
                            <p>Описание</p>
                            <textarea ref={goodDescription}></textarea>
                        </div>
                    </div>
                    <div className={ `${styles.myGoodButtonsContainer}` }>
                        <button className={ `${styles.logoutButton} ${styles.deleteGoodButton}` } onClick={hadlecreateGoodFormClose}>Удалить товар</button>
                        <CustomButton className={ `${styles.saveButton} ${styles.createGoodButton}` } onClick={handleCreateGood}>Создать товар</CustomButton>
                    </div>
                    </>
                );
            } else {
                return(
                    <>
                    <div className={ `${styles.myGoodsContainer}`}>
                        <ul className={ `${styles.myGoodsList}`}>
                            {getMyGoodsTemplate(myGoods)}
                        </ul>
                    </div>
                    <CustomButton className={ `${styles.saveButton} ${styles.createGoodButton}` } onClick={hadlecreateGoodFormOpen}>Добавить товар</CustomButton>
                    </>
                );
            }
        case ACCOUNT_CONTENT.ANALITICS:
            return(
                <>
                АНАЛИТИКА
                </>
            );
        case ACCOUNT_CONTENT.USERS:
            const {users, handleSelectUserClick, seletedUser, handlePromoteSeller} = props;
            let userRole = null;
            if (seletedUser) userRole = seletedUser.userRole;
            return(
                <>
                <div className={ `${styles.myGoodsContainer}`}>
                    <table className={ `${styles.table}`}>
                        <thead>
                            <tr className={ `${styles.tableContainer} ${styles.tableHeader}`}>
                            <td>Имя</td>
                            <td>Статус</td>
                            </tr>
                        </thead>
                        <tbody className={ `${styles.tableBody}`} onClick={handleSelectUserClick}>
                            {getUsersTemplate(users, seletedUser)}
                        </tbody>
                    </table>
                </div>
                <button className={ `${styles.promoteSellerButton}` } onClick={handlePromoteSeller} disabled={(userRole !== USER_ROLE.USER) || (userRole === null)}>Сделать продавцом</button>
                </>
            );
    }
};

const getProfileRoutes = (userRole, currentContent) => {
    switch(userRole) {
        case USER_ROLE.USER:
            return(
                <>
                <li className={ `${styles.navListItem}` }><a data-content={ACCOUNT_CONTENT.PRESONAL_INFORMATION} href='#' className={ `${styles.navListItemLink} ${ (currentContent === ACCOUNT_CONTENT.PRESONAL_INFORMATION) ? styles.navListItemActive : '' }` }>Личная информация</a></li>
                <li className={ `${styles.navListItem}` }><a data-content={ACCOUNT_CONTENT.REVIEWS} href='#' className={ `${styles.navListItemLink} ${ (currentContent === ACCOUNT_CONTENT.REVIEWS) ? styles.navListItemActive : '' }` }>Отзывы</a></li>
                </>
            );
        case USER_ROLE.SELLER:
            return(
                <>
                <li className={ `${styles.navListItem}` }><a data-content={ACCOUNT_CONTENT.PRESONAL_INFORMATION} href='#' className={ `${styles.navListItemLink} ${ (currentContent === ACCOUNT_CONTENT.PRESONAL_INFORMATION) ? styles.navListItemActive : '' }` }>Личная информация</a></li>
                <li className={ `${styles.navListItem}` }><a data-content={ACCOUNT_CONTENT.MY_GOODS} href='#' className={ `${styles.navListItemLink} ${ (currentContent === ACCOUNT_CONTENT.MY_GOODS) ? styles.navListItemActive : '' }` }>Мои товары</a></li>
                <li className={ `${styles.navListItem}` }><a data-content={ACCOUNT_CONTENT.ANALITICS} href='#' className={ `${styles.navListItemLink} ${ (currentContent === ACCOUNT_CONTENT.ANALITICS) ? styles.navListItemActive : '' }` }>Аналитика</a></li>
                </>
            );
        case USER_ROLE.ADMIN:
            return(
                <>
                <li className={ `${styles.navListItem}` }><a data-content={ACCOUNT_CONTENT.PRESONAL_INFORMATION} href='#' className={ `${styles.navListItemLink} ${ (currentContent === ACCOUNT_CONTENT.PRESONAL_INFORMATION) ? styles.navListItemActive : '' }` }>Личная информация</a></li>
                <li className={ `${styles.navListItem}` }><a data-content={ACCOUNT_CONTENT.USERS} href='#' className={ `${styles.navListItemLink} ${ (currentContent === ACCOUNT_CONTENT.USERS) ? styles.navListItemActive : '' }` }>Пользователи</a></li>
                </>
            );
    }
};

const Profile = () => {
    const {setIsAuthenticated, userRole, setUserRole, setCartCount, setOrdersCount, userName} = useAuth();
    const [currentContent, setCurrentContent] = useState( ACCOUNT_CONTENT.PRESONAL_INFORMATION);
    // Зависимость от роли
    // SELLER
    const [myGoods, setMyGoods] = useState(null);
    const [isCreateGoodFormOpen, setIsCreateGoodFormOpen] = useState(false);
    const [categories, setCategories] = useState(null);
    const goodName = useRef();
    const goodPhoto = useRef();
    const goodPrice = useRef();
    const goodQuantity = useRef();
    const goodCategory = useRef();
    const goodDescription = useRef();
    // ADMIN
    const [users, setUsers] = useState(null);
    const [seletedUser, setSeletedUser] = useState(null);

    const handleSave = () => {

    };

    const handleLogout = () => {
        logout(redirect, setIsAuthenticated, setUserRole, setCartCount, setOrdersCount);
    };

    const handleProfileContentChange = (evt) => {
        evt.preventDefault();
        if (evt.target.tagName === 'A') {
            setCurrentContent(evt.target.dataset.content);
        }
    };

    const handleSelectUserClick = (evt) => {
        if (!['TD', 'TR'].includes(evt.target.tagName)) {
            return;
        }

        if (evt.target.tagName === 'TD') {
            const element = evt.target.closest('tr');
            if (seletedUser && (seletedUser.userId === element.id)) {
                setSeletedUser(null);
            } else {
                setSeletedUser({userId: element.id, userRole: element.dataset.role});
            }
            return;
        }
        if (seletedUser && (seletedUser.userId === evt.target.id)) {
            setSeletedUser(null);
        } else {
            setSeletedUser({userId: evt.target.id, userRole: evt.target.dataset.role});
        }
    };

    const handlePromoteSeller = () => {
        promoteSeller({userId: seletedUser.userId, users, setUsers});
    };

    useEffect(() => {
        if (userRole === USER_ROLE.SELLER) {
            getMyGoods({setMyGoods});
            getCategories({setCategories});
        } else if (userRole === USER_ROLE.ADMIN) {
            getUsers({setUsers});
        }
    }, [userRole]);

    return(
        <>
        <h1 className={ `${styles.pageHeader}` }>Личный кабинет {USER_ROLE_NAME[userRole]}</h1>
        <div className={ `${styles.pageContentContainer}` }>
            <div className={ `${styles.navPanel}` }>
                <div className={ `${styles.navWrapper}` }>
                    <div className={ `${styles.navPanelInfo}` }>
                        <div className={ `${styles.avatar}` }></div>
                        <p className={ `${styles.navPanelName}` }>{userName}</p>
                    </div>
                    <div className={ `${styles.navListContainer}` }>
                        <nav>
                            <ul className={ `${styles.navList}` } onClick={handleProfileContentChange}>
                                {getProfileRoutes(userRole, currentContent)}
                            </ul>
                        </nav>
                    </div>
                </div>
                <button className={ `${styles.logoutButton}` } onClick={handleLogout}>Выйти</button>
            </div>
            <div className={ `${styles.currentContentContainer}` }>
                {getCurrentContent(currentContent, {handleSave, myGoods, isCreateGoodFormOpen, setIsCreateGoodFormOpen, users, setSeletedUser, seletedUser, handleSelectUserClick, handlePromoteSeller, goodName, goodPhoto, goodPrice, goodQuantity, goodCategory, goodDescription, categories})}
            </div>
        </div>
        </>
    );
};

export default Profile;