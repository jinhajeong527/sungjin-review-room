import env from './env.js';
import { getJWTFromCookie } from './auth.js';

const doorIcon = document.querySelector('header .main .enterBtn i');
const preferred = document.querySelector('main .contents .category #preferred .reviews');
const monthlyBest = document.querySelector('main .contents .category #monthlyBest .reviews');
const wishList = document.querySelector('main .contents .category #wishList .reviews');

const preferredPrev = document.querySelector('main .contents .category #preferred .page__btn__box .prev__page__btn');
const preferredNext = document.querySelector('main .contents .category #preferred .page__btn__box .next__page__btn');
const monthlyBestPrev = document.querySelector('main .contents .category #monthlyBest .page__btn__box .prev__page__btn');
const monthlyBestNext = document.querySelector('main .contents .category #monthlyBest .page__btn__box .next__page__btn');
const wishListPrev = document.querySelector('main .contents .category #wishList .page__btn__box .prev__page__btn');
const wishListNext = document.querySelector('main .contents .category #wishList .page__btn__box .next__page__btn');

let preferredPageNo = 1;
let monthlyBestPageNo = 1;
let wishListPageNo = 1;
let defaultPageSize = 3;
let endPageNo = 5;

window.onload = setMainList();

function getPagePayload(pageNum) {
    const pagePayload = {
        pageNumber: pageNum,
        pageSize: defaultPageSize
    }
    return pagePayload
}

function getPreferredList() {
    let pagePayload = getPagePayload(preferredPageNo);
    axios.get(`http://${env.api_address}/api/show/preferred`, pagePayload)
    .then(function(response) {
        console.log(response);
        addItemListToParent(preferred, response.data.show)
    })
    .catch(function (error) {
        console.log(error);
    })
}

function prevPreferredPage() {
    if (preferredPageNo == 1) {
        return
    } else {
        preferredPageNo--
        getPreferredList();
    }
}

function nextPreferredPage() {
    if (preferredPageNo == endPageNo) {
        return
    } else {
        preferredPageNo++
        getPreferredList();
    }
}

function getMonthlyBestList() {
    let pagePayload = getPagePayload(monthlyBestPageNo);
    axios.get(`http://${env.api_address}/api/show/mostReviewed`, pagePayload)
    .then(function(response) {
        console.log(response);
        addItemListToParent(monthlyBest, response.data.show)
    })
    .catch(function (error) {
        console.log(error);
    })
}


function prevMonthlyPage() {
    if (monthlyBestPageNo == 1) {
        return
    } else {
        monthlyBestPageNo--
        getMonthlyBestList();
    }
}

function nextMonthlyPage() {
    if (monthlyBestPageNo == endPageNo) {
        return
    } else {
        monthlyBestPageNo++
        getMonthlyBestList();
    }
}

function getWistList() {
    let pagePayload = getPagePayload(wishListPageNo);
    axios.get(`http://${env.api_address}/api/show/wishlist`, pagePayload)
    .then(function(response) {
        console.log(response);
        addItemListToParent(wishList, response.data.show)
    })
    .catch(function (error) {
        console.log(error);
    })
}


function prevWishListPage() {
    if (wishListPageNo == 1) {
        return
    } else {
        wishListPageNo--
        getWistList();
    }
}

function nextWishListPage() {
    if (wishListPageNo == endPageNo) {
        return
    } else {
        wishListPageNo++
        getWistList();
    }
}

function setMainList() {
    if (getJWTFromCookie()!="") {
        getPreferredList();
        getWistList();
    }
    getMonthlyBestList();
}

function getReviewPreviewHTML(element) {
    return `
    <li class="item">
    <img src="${element.img}" alt="reviewItem">
    <span class="item__title">${element.name} <button class="wishlist__btn" onclick="addWishList(${element.id})"><i class="fa-solid fa-cart-shopping"></i></button></span>
    <span class="item__reviews">${element.reviewsCount} views</span>
    </li> 
    `
}

function getNoReviewHTML() {
    return `
    <li class="item">
    리뷰가 존재하지 않습니다.
    </li> 
    `
}

function addItemListToParent(parent, list) {
    if (list.length == 0) {
        parent.appendChild(getNoReviewHTML())
        let pageBtn;
        switch (parent) {
            case preferred:
                pageBtn = document.querySelector('main .contents .category #preferred .page__btn__box');
                break;
            case monthlyBest:
                pageBtn = document.querySelector('main .contents .category #monthlyBest .page__btn__box');
                break;
            case wishList:
                pageBtn = document.querySelector('main .contents .category #wishList .page__btn__box');
                break;
        }
        pageBtn.style.display = none;
    } else {
    list.forEach(element => {
        let reviewItem = getReviewPreviewHTML(element)
        parent.appendChild(reviewItem)
    })
    }
}

function registerPopup() {
    window.open("review_register/review_register.html", "review_register_popup")
}

function loginPopup() {
    window.open("login/login.html", "login_popup")

}

function signupPopup() {
    window.open("signup/signup.html", "signup_popup")

}

function addWishList(showId) {
    const wishListPayload = {showId: showId};
    axios.post(`http://${env.api_address}/api/reviewer/wishlist`, wishListPayload)
    .then(function(response) {
        console.log(response);
    })
    .catch(function (error) {
        console.log(error);
        alert("위시리스트에 추가되었습니다.")
    })
}