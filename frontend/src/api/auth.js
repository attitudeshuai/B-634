import axios from './index';

export function login(username, password) {
  return axios.post('/auth/login', { username, password });
}

export function getCurrentUser() {
  return axios.get('/auth/me');
}
