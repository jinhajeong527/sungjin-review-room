import env from '../env.js';
const signUpBtn = document.querySelector('.signup__content .signup__btn');
const genreList = document.querySelector('.genres .genre__list')
let genreSet = new Set();

async function getGenres() {
  await axios.get(`http://${env.api_address}/api/genre/genres`)
  .then(function (response) {
    console.log(response);
    setGenresToHTML(response.data);
  })
  .catch(function (error) {
    console.log(error);
    alert("장르를 불러오는데 실패하였습니다.")
  }) 
}

window.onload = getGenres();

function setGenresToHTML(genres) {
  genres.forEach(element => {
    genreItem = getGenreItem(element);
    genreList.appendChild(genreItem);
  })
}

function getGenreItem(element) {
  let genreItem = document.createElement("li");
  genreItem.className="genre";
  genreItem.innerHTML = getGenreItemInnerHTML(element);
  return genreItem
}

function getGenreItemInnerHTML(element) {
  return `
  <input onclick="check(this)" type="checkbox" id="${element.name}" value="${element.id}" name="genres">
  <label for="${element.name}">${element.name}</label>
  `
}

//genres change일어날떄마다 1.개수 check -> 3개까지만 선택할 수 있습니다.
function check(element) {
  const id = element.id
  const checkbox = document.getElementById(id)
  let checked = checkbox.checked
  if (!checked) {
      genreSet.delete(id)
  } else {
      if (genreSet.size < 3) {
          genreSet.add(id)
      } else {
          alert("선호하는 장르는 3개 이상 선택할 수 없습니다");
          checkbox.checked = false;
          return false
      }
  }
}


signUpBtn.addEventListener('click', () => {
   let email = document.getElementById('email').value;
   let password = document.getElementById('password').value;
   let firstName = document.getElementById('firstName').value;
   let lastName = document.getElementById('lastName').value;
   let mbti = document.getElementById('mbti').value;
    if (genreSet.size > 3) { //여기서 한번더 체크할 필요 없는 것 같은데...
      alert("선호하는 장르는 3개 이상 선택할 수 없습니다");
      return false
    }
   let user = {
       email: email,
       password: password,
       firstName: firstName,
       lastName: lastName,
       mbti: mbti,
       genres: genreSet
   }
    signup(user)
})

function signup(user) {
    axios.post(`http://${env.api_address}/api/auth/signup`, user)
      .then(function (response) {
        console.log(response);
        alert("입력하신 이메일로 인증 메일이 발송 되었습니다.");
      })
      .catch(function (error) {
        console.log(error);
        alert("회원가입 실패")
      });
}

