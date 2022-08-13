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

const showDetailModal = document.querySelector('.review__detail__modal')

let preferredPageNo = 1;
let monthlyBestPageNo = 1;
let wishListPageNo = 1;
let defaultPageSize = 3;
let endPageNo = 5;

window.onload = setMainList();

function getPageParam(pageNum) {
    return {
        pageNumber: pageNum,
        pageSize: defaultPageSize
    }
}

function getPreferredList() {
    axios.get(`http://${env.api_address}/api/show/preferred`, { params: getPageParam(preferredPageNo) })
    .then(function(response) {
        console.log(`preferred ${response.data}`)
        addItemListToParent(preferred, response.data)
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
    axios.get(`http://${env.api_address}/api/show/mostReviewed`, { params: getPageParam(monthlyBestPageNo) })
    .then(function(response) {
        console.log(`monthly best: ${response.data}`)
        // list 부분인 response.data.show의 수정이 필요하다. undefined 에러. 실제 response 데이터 예시는 노션에 정리했다. data에서 바로 show로 찍고 들어갈 수 없는 것이 이슈이다.
        addItemListToParent(monthlyBest, response.data);
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
    axios.get(`http://${env.api_address}/api/show/wishlist`, { params: getPageParam(wishListPageNo) })
    .then(function(response) {
        console.log(`wishlist : ${response.data}`)
        addItemListToParent(wishList, response.data)
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
    <img src="${element.show.img}" alt="reviewItem" class="show__image" onclick="showDetail(${element.show.id})">
    <span class="item__title">${element.show.name} <button class="wishlist__btn" onclick="addWishList(${element.show.id})"><i class="fa-solid fa-cart-shopping"></i></button></span>
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

//todo fill url
const showDetailUrl = `http://${env.api_address}/...`

function showDetail(showId) {
    let showDetailPayload = {id: showId} //todo
    axios.get(showDetailUrl, showDetailPayload)
    .then(function(response) {
        console.log(response);
        openShowDetailModal(response);
    })
    .catch(function (error) {
        console.log(error);
    })
}

function openShowDetailModal(showDetailResponse) {
    let htmlTemplate = getShowDetailModalTemplate(showDetailResponse)
    showDetailModal.innerHTML = htmlTemplate
    appendReviewListToShowDetailModal(showDetailResponse.reviewList)
    let closeBtn = document.querySelector('.review__detail__modal__close__btn')
    closeBtn.addEventListener('click', closeShowDetailModal)
    showDetailModal.style.display = 'flex';
}

function closeShowDetailModal() {
    showDetailModal.innerHTML = '';
    showDetailModal.style.display = 'none';
}

function getShowDetailModalTemplate(showDetailResponse) {
    return `
        <div class="review__detail__modal__content">
            <button class="review__detail__modal__close__btn"><i class="fa-solid fa-xmark"></i></button>
            <div class="show__info">
            <img src="${showDetailResponse.imageSrc}" class="show__detail__image">
            <div class="show__detail__info">
                <span class="show__detail__title">${showDetailResponse.title}</span>
                <span class="show__detail__review__cnt">${showDetailResponse.reviewCnt} views</span>
            </div>
        </div>
        <div class="review__content">
            <span>리뷰</span>
            <ul class="show__detail__review__list">
            </ul>
        </div>
    `
}

function appendReviewListToShowDetailModal(reviewList) {
    let reviewContent = document.querySelector('.review__content .show__detail__review__list');
    for (let review in reviewList) {
        let liNode = document.createElement("li")
        let liText = document.createTextNode(review)
        liNode.append(liText)
        reviewContent.appendChild(liNode)
    }
}

