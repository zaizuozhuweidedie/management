import request from '@/utils/request'

/**
 * 部门列表查询 GET /depts
 */
export function getDeptList() {
  return request.get('/depts')
}

/**
 * 根据ID删除部门 DELETE /depts?id=xxx
 */
export function deleteDept(id) {
  return request.delete('/depts', { params: { id } })
}

/**
 * 添加部门 POST /depts
 * @param {string} name 部门名称
 */
export function addDept(name) {
  return request.post('/depts', { name })
}

/**
 * 根据ID查询部门 GET /depts/{id}
 */
export function getDeptById(id) {
  return request.get(`/depts/${id}`)
}

/**
 * 修改部门 PUT /depts
 * @param {{ id: number, name: string }} data
 */
export function updateDept(data) {
  return request.put('/depts', data)
}
