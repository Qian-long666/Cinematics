const TOKEN_KEY = 'cinematics_token'
const USERNAME_KEY = 'cinematics_username'
const ROLE_KEY = 'cinematics_role'

export function getToken() {
    return sessionStorage .getItem(TOKEN_KEY)
}

export function setAuth(token, username, role) {
    sessionStorage .setItem(TOKEN_KEY, token)
    sessionStorage .setItem(USERNAME_KEY, username)
    sessionStorage .setItem(ROLE_KEY, role)
}

export function clearAuth() {
    sessionStorage .removeItem(TOKEN_KEY)
    sessionStorage .removeItem(USERNAME_KEY)
    sessionStorage .removeItem(ROLE_KEY)
}

export function isLoggedIn() {
    return !!getToken()
}

export function isAdmin() {
    return sessionStorage .getItem(ROLE_KEY) === 'ADMIN'
}

export function getUsername() {
    return sessionStorage .getItem(USERNAME_KEY)
}
