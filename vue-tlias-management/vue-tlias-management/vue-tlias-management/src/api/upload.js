import request from '@/utils/request'
import axios from 'axios'

/**
 * 上传文件 POST /upload (multipart/form-data)
 * 为防止 token 拦截器影响，单独发请求
 */
export function uploadFile(file) {
  const form = new FormData()
  form.append('file', file)

  const token = localStorage.getItem('token')
  const headers = {}
  if (token) headers.token = token

  return axios.post('/api/upload', form, { headers })
}
