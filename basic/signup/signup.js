const signUpBtn = document.querySelector('.signup__content .signup__btn');
/*todo: 3개이상 넘기지 못하게하는 로직 추가 */
signUpBtn.addEventListener('click', () => {
   let email = document.getElementById('email').value;
   let password = document.getElementById('password').value;
   let firstName = document.getElementById('firstName').value;
   let lastName = document.getElementById('lastName').value;
   let mbti = document.getElementById('mbti').value;
  //  let genreSet = new Set();
  //  let genres = document.querySelectorAll('.signup__form .genres .genre__list input[name=genres]')
  //  for (let i =0 ; i < genres.length; i++) {
  //      if (genres[i].checked) {
  //       genreSet.add(genres[i].value);
  //      }
  //  }
   let user = {
       email: email,
       password: password,
       firstName: firstName,
       lastName: lastName,
       mbti: mbti,
       genres: [
        {
          id:16,
          name:"애니메이션"
       },
       {
          id:18,
          name:"드라마"
       }
       ]
   }
    signup(user)
})

function signup(user) {
    axios.post('http://localhost:8080/api/auth/signup', user)
      .then(function (response) {
        console.log(response);
        alert("회원가입 성공");
      })
      .catch(function (error) {
        console.log(error);
        alert("회원가입 실패")
      });
}

