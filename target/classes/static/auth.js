const jwtCookieKey = "sungjinJWT";

export function getJWTFromCookie() { 
    return getCookie(jwtCookieKey);
}

export function getCookie(key) {
    let cookieKey = key + "="; 
    let result = "";
    const cookieArr = document.cookie.split(";");
    // 브라우저에서 확인 했을 때 액세스 토큰 및 리프레쉬 토큰을 쿠키에서 확인할 수 있었는데, 여기서는 확인할 수 없다.. 왜일까..?
    console.log(cookieArr);
    for(let i = 0; i < cookieArr.length; i++) {
        cookieArr[i] = cookieArr[i].trim();

      if(cookieArr[i].startsWith(cookieKey)) {
        result = cookieArr[i].slice(cookieKey.length, cookieArr[i].length);
        return result;
      }
    }
    return ""
}
 
export default {getJWTFromCookie, getCookie}