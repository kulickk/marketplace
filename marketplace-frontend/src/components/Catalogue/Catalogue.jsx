'use client'
import { useEffect, useState } from 'react';
import styles from './Catalogue.module.css'
import { getCategories } from '@/utils/requests';
import { frontendRouter } from '@/utils/const';

const categorySubmenu = (childCategories) => {
    return childCategories.map((childCategory) => {
        return(
            <li key={childCategory.id} className={ `${styles.catalogueListItem}` }><a href={ frontendRouter.SEARCH_CATEGORY(childCategory.id) }>{childCategory.name}</a></li>
        );
    });
};

const categoryList = (categories) => {
    return categories.map((category) => {
        if (category.children.length > 0) {
            return(
                <li key={category.id} className={ `${styles.catalogueListItem}` }><a href={ frontendRouter.SEARCH_CATEGORY(category.id) }>{category.name}</a>
                    <ul className={ `${styles.catalogueSubMenu}` }>
                        {categorySubmenu(category.children)}
                    </ul>
                </li>
            );
        }
        return(
            <li key={category.id} className={ `${styles.catalogueListItem}` }><a href={ frontendRouter.SEARCH_CATEGORY(category.id) }>{category.name}</a></li>
        );
    });
};

const Catalogue = ({onClose}) => {
    const [categories, setCategories] = useState([]);

    const handleClickFormClose = (evt) => {
        if (evt.target.tagName === 'SECTION' || evt.target.tagName === 'DIV') {
            onClose();
        }
    };

    useEffect(() => {
        getCategories({setCategories});
    }, []);
    
    return (
        <section className={ `${styles.section}` } onClick={handleClickFormClose}>
            <div className={ `${styles.catalogueContainer}` }>
                <ul className={ `${styles.catalogueList}` }>
                    {categoryList(categories)}
                </ul>
            </div>
        </section>
    );
};

export default Catalogue;