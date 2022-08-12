import env from '../env.js';
const mailResendBtn = document.querySelector('.auth__mail__btn');
mailResendBtn.addEventListener('click', () => {
    let paramMap = getParamMapFromURL(window.location.href);
    console.log(paramMap); // ex) Map { token → "a5905de8-39a7-4d8b-9be8-5135641b6f74" }
    //let resendToken = paramMap.get('resendToken');
    let resendToken = paramMap.get('token');
    axios.get(`http://${env.api_address}/api/auth/resendToken?token=${resendToken}`)
    .then(function (response) {
        console.log(response);
        alert("인증 메일이 재발송 되었습니다.")
    })
    .catch(function (error) {
        console.log(error);
        alert("인증메일 재발송 실패")
    })
})

function getParamMapFromURL(url) {
    let urlObj = new URL(url);
    let params = urlObj.search;
    if (params.charAt(0) == "?") {
        params = params.slice(1,params.length);
    }
    let paramSlice = params.split('&');
    let map = new Map();
    for (let i = 0 ; i<paramSlice.length; i++) {
        let s = paramSlice[i].split('=');
        map.set(s[0],s[1])
    }
    return map
}

