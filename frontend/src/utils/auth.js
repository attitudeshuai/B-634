const TOKEN_KEY = 'campus_token';
const USER_KEY = 'campus_user';

export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token);
}

export function removeToken() {
  localStorage.removeItem(TOKEN_KEY);
}

export function getUser() {
  const userStr = localStorage.getItem(USER_KEY);
  return userStr ? JSON.parse(userStr) : null;
}

export function setUser(user) {
  localStorage.setItem(USER_KEY, JSON.stringify(user));
}

export function removeUser() {
  localStorage.removeItem(USER_KEY);
}

export function isAdmin() {
  const user = getUser();
  return user && user.role === 'ADMIN';
}

export function isLoggedIn() {
  return !!getToken();
}

export function logout() {
  removeToken();
  removeUser();
}
