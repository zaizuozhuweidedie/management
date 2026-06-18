import request from '@/utils/request'

/**
 * 员工登录 POST /login
 * @param {{ username: string, password: string }} data
 */
export function login(data) {
  return request.post('/login', data)
}
