import request from '@/utils/request'

/**
 * 班级列表查询（条件分页） GET /clazzs
 */
export function getClazzList(params) {
  return request.get('/clazzs', { params })
}

/**
 * 删除班级 DELETE /clazzs/{id}
 */
export function deleteClazz(id) {
  return request.delete(`/clazzs/${id}`)
}

/**
 * 添加班级 POST /clazzs
 */
export function addClazz(data) {
  return request.post('/clazzs', data)
}

/**
 * 根据ID查询班级 GET /clazzs/{id}
 */
export function getClazzById(id) {
  return request.get(`/clazzs/${id}`)
}

/**
 * 修改班级 PUT /clazzs
 */
export function updateClazz(data) {
  return request.put('/clazzs', data)
}

/**
 * 查询所有班级 GET /clazzs/list
 */
export function getAllClazzs() {
  return request.get('/clazzs/list')
}
