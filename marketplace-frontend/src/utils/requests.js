const { marketApi, frontendRouter, USER_ROLE } = require("./const");

const logout = (redirect, isAuthentificated, setUserRole, setCartCount, setOrdersCount) => {
    fetch(marketApi.AUTH.LOGOUT, {
        method: 'POST',
        credentials: 'include'
    }).then(response => {
        if (response.ok) {
            isAuthentificated(false);
            setUserRole(null);
            setCartCount(null); 
            setOrdersCount(null);
            redirect(frontendRouter.INDEX);
        }
    });
};

const getGoods = ({page = 0, size = 30, goods, setGoods}) => {
    fetch(marketApi.GOODS.GET + `?page=${page}&size=${size}`).then(response => {
        if (response.ok) {
            return response.json();
        }
    }).then(data => setGoods(data));
};

const getCategories = ({setCategories}) => {
    fetch(marketApi.GOODS_CATEGORY.GET).then(response => {
        if (response.ok) {
            return response.json();
        }
    }).then(data => setCategories(data));
};

const getCartGoods = ({setGoods}) => {
    fetch(marketApi.BASKET.GET, {
        credentials: 'include'
    }).then(response => {
        if (response.ok) {
            return response.json();
        }
    }).then(data => setGoods(data.items.map(good => ({
        ...good,
        price: Math.floor(good.price),
        selected: false,
        count: good.quantity
    }))));
};

const getProductInfo = ({id, setImageUrls, setProduct, isAuthenticated, setBigPicture}) => {
    fetch(marketApi.GOODS.GET_BY_ID(id)).then(response => {
        if (response.ok) {
            return response.json();
        }
    }).then(data => {
        setImageUrls(data.imagePaths);
        setBigPicture(data.imagePaths[0]);
        if (isAuthenticated) {
            fetch(marketApi.BASKET.GET, {
            credentials: 'include'
        }).then(response => {
            if (response.ok) {
                return response.json();
            };
        }).then(cartData => setProduct({...data, isInCart: cartData.items.some(cartItem => cartItem.goodId === data.id)}));
        } else {
            setProduct({...data, isInCart: null});
        }
    });
};

const addToCart = ({id, redirect, setCartCount}) => {
    const requestData = {
        "goodId": id,
        "quantity": 1
    }

    fetch(marketApi.BASKET.ADD_GOOD, {
        method: 'POST',
        credentials: 'include',
        body: JSON.stringify(requestData),
        headers: {
            'Content-type': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            setCartCount(prevCount => prevCount + 1);
            redirect(frontendRouter.CART);
        }
    });
};

const deleteFromCart = ({id, handleDeleteGood}) => {
    fetch(marketApi.BASKET.DELETE_GOOD(id), {
        method: 'DELETE',
        credentials: 'include'
    }).then(response => {
        if (response.ok) {
            handleDeleteGood(id);
        }
    });
};

const createOrder = ({goodIds, redirect, setOrdersCount}) => {
    const requestData = {
    "basketItemIds": goodIds,
    "recipientFirstName": "Иван",
    "recipientLastName": "Петров",
    "recipientPhone": "+79123456789",
    "pickupAddress": "ул. Ленина, д. 10, ПВЗ №5"
    };

    fetch(marketApi.ORDERS.CREATE, {
        method: 'POST',
        credentials: 'include',
        body: JSON.stringify(requestData),
        headers: {
            'Content-type': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            setOrdersCount(prev => prev + 1);
            redirect(frontendRouter.ORDERS);
        }
    });
};

const updateCartQuantity = ({id, goodId, quantity}) => {
    const requestData = {
        "goodId": goodId,
        "quantity": quantity
    };

    fetch(marketApi.BASKET.UPDATE_QUANTITY(id), {
        method: 'PATCH',
        credentials: 'include',
        body: JSON.stringify(requestData),
        headers: {
            'Content-type': 'application/json'
        }
    });
};

const getOrders = ({setOrders}) => {
    fetch(marketApi.ORDERS.GET, {
        credentials: 'include'
    }).then(response => {
        if (response.ok) {
            return response.json();
        }
    }).then(data => {
        console.log(data);
        setOrders(data);
    });
};

const getMyGoods = ({setMyGoods}) => {
    fetch(marketApi.GOODS.GET_MY_GOODS, {
        credentials: 'include'
    }).then(response => {
        if (response.ok) {
            return response.json();
        }
    }).then(data => setMyGoods(data));
};

const getUsers = ({setUsers}) => {
    fetch(marketApi.ADMIN.USERS, {
        credentials: 'include'
    }).then(response => {
        if (response.ok) {
            return response.json();
        }
    }).then(data => setUsers(data));
};

const promoteSeller = ({userId, users, setUsers}) => {
    fetch(marketApi.ADMIN.PROMOTE_SELLER(userId), {
        method: 'PATCH',
        credentials: 'include'
    }).then(response => {
        if (response.ok) {
            const newUsers = users.map(user => {
                if (user.id === userId) {
                    return {
                        ...user,
                        role: USER_ROLE.SELLER
                    };
                }
                return user;
            });
            setUsers(newUsers);
        }
    });
};

const getUserInfo = ({setIsAuthenticated, setUserRole, setCartCount, setOrdersCount, setUserName}) => {
    fetch(marketApi.USER.ME, {
    credentials: 'include'
    }).then(response => {
    if (response.ok && response.status !== 401) {
        console.log(123);
        return response.json();
    }
    setIsAuthenticated(false);
    throw new Error('User is unauthorized');
    }).then(data => {
    setIsAuthenticated(true);
    setUserRole(data.role);
    setUserName(`${data.firstName} ${data.lastName}`);
    
    fetch(marketApi.ORDERS.GET, {
        credentials: 'include'
    }).then(response => {
        if (response.ok) {
        return response.json();
        }
    }).then(data => {
        setOrdersCount(data.length);
    });

    fetch(marketApi.BASKET.GET, {
        credentials: 'include'
    }).then(response => {
        if (response.ok) {
        return response.json();
        }
    }).then(data => {
        setCartCount(data.items.length);
    });
    }).catch(err => {
        console.log(err)
    })
};

const getGoodsByName = ({goodName, setGoods}) => {
    fetch(marketApi.GOODS.SEARCH_BY_NAME(goodName)).then(response => {
        if (response.ok) {
            return response.json();
        }
    }).then(data => setGoods(data));
};

const getGoodsByCategory = ({categoryId, setGoods, setCategoryName}) => {
    fetch(marketApi.GOODS.SEARCH_BY_CATEGORY(categoryId)).then(response => {
        if (response.ok) {
            return response.json();
        }
    }).then(data => {
        setGoods(data);
        fetch(marketApi.GOODS_CATEGORY.GOODS(categoryId)).then(response => {
            if (response.ok) {
                return response.json();
            }
        }).then(categoryData => setCategoryName(categoryData.name))
    });
};

const createGood = ({good, images}) => {
    const requestBody = {
        "name": good.name,
        "description": good.description,
        "price": good.price,
        "stock": good.quantity,
        "brand": "Samsung",
        "categoryId": good.categoryId
    }

    fetch(marketApi.GOODS.CREATE, {
        credentials: 'include',
        method: 'POST',
        body: JSON.stringify(requestBody),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            return response.json();
        }
    }).then(data => {
        Array.from(images).forEach(image => {
            const formData = new FormData();
            formData.append('file', image);
            fetch(marketApi.GOODS.UPLOAD_IMAGE(data.id), {
                method: 'POST',
                credentials: 'include',
                body: formData
            })
        });
    });
}; 

export {logout, getGoods, getCategories, getCartGoods, getProductInfo, addToCart, deleteFromCart, createOrder, updateCartQuantity, getOrders, getMyGoods, getUsers, promoteSeller, getUserInfo, getGoodsByName, getGoodsByCategory, createGood}