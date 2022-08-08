const jwtCookieKey = "sungjinJWT";

export function getJWTFromCookie() { 
    return getCookie(jwtCookieKey);
}

export function getCookie(key) {
    let cookieKey = key + "="; 
    let result = "";
    const cookieArr = document.cookie.split(";");
    
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