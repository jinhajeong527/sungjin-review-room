

const reviewRegisterModal = document.querySelector('.review__register__modal');
const reviewRegisterModalBtn = document.querySelector('.review__register__btn');
const showSearchInput = document.querySelector('.review__register__content .show__search__content #title');
const tmdbUrl = "https://api.themoviedb.org/3";
const defaultParams = "api_key=c41a023bef1a05820bd3a88f59116982&language=ko-KR";
const showList = document.querySelector('.review__register__content .show__search__content .show__search__list')
const show = document.querySelector('.review__register__content .show__search__content .show__search__list li')
const reviewRegisterBtn = document.querySelector('.review__register__modal .review__register__content button')
const postIcon = document.querySelector('.review__register__modal .review__register__content i')
const color = document.querySelector('.review__register__modal .review__register__content .review__register__form #color'); 
const currentReview = document.querySelector('.review__register__modal .review__register__content .review__register__form #review'); 
let selectedShow;

reviewRegisterModalBtn.addEventListener('click', () => {
    reviewRegisterModal.style.display = 'flex';
});

showSearchInput.addEventListener('input', () => {
    showList.style.display = 'block';
    searchTVShows(showSearchInput.value);
})

reviewRegisterBtn.addEventListener('mouseover', () => {
    postIcon.style.color= color.value;
})

reviewRegisterBtn.addEventListener('mouseleave', () => {
    postIcon.style.color= 'black';
})

/*
{
   "review":{
      "comment":"so good"
   },
 
   "show":{
      "id":100,
      "name":"test show"
   },
   
   "genres":[
      {
         "id":16,
      },
      {"id":18}]
}
*/

reviewRegisterBtn.addEventListener('click', () => {
    let review = {
        comment: currentReview.value
    }
    let show = {
        id: selectedShow.id,
        name: selectedShow.name
    }
    let genres = [];
    selectedShow.genre_ids.forEach(id => {
        let genre = {
            id: id
        }
        genres.push(genre)
    })
    
    let reviewPayload = {
        review: review,
        show: show,
        genres: genres
    }

    axios.post('http://localhost:5500/api/review', reviewPayload)
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
})

function searchTVShows(title) {
    showList.textContent = '';
    if (title.length != 0) {
        axios.get(tmdbUrl + "/search/tv?" + defaultParams + "&page=1&query=" + title)
        .then(function(response) {
            response.data.results.forEach(element => {
                let showItem = getShowItem(element);
                showItem.addEventListener('click', () => {showClickEvent(element)});
                showList.appendChild(showItem);
            });
        })
        .catch(function(err) {
            console.log(err)
        });
    } else {
        showList.style.display = 'none';
    }
}

function showClickEvent(element) {
    let item = getShowItem(element);
    showList.textContent = '';
    showList.appendChild(item);
    selectedShow = element;
    console.log(element)
}

function getShowItem(element) {
    if (element.poster_path == null) {
        element.poster_path = "../src/img/noimage.jpeg"
    } else {
        element.poster_path = "https://image.tmdb.org/t/p/original/" + element.poster_path
    }
    let showItem = document.createElement("li");
    showItem.className="show";
    showItem.id=element.id;
    showItem.innerHTML = getShowItemInnerHTML(element);
    return showItem
}

function getShowItemInnerHTML(element) {
    return `
    <img src="${element.poster_path}" width="50"/>
    <div class="show__title">
    <span>${element.name}</span>
    <span>(원제:${element.original_name})</span>
    </div>
    `
}