import request from '@/utils/request'

/**
 * 员工列表查询（条件分页） GET /emps
 */
export function getEmpList(params) {
  return request.get('/emps', { params })
}

/**
 * 批量删除员工 DELETE /emps?ids=1,2,3
 */
export function deleteEmps(ids) {
  return request.delete('/emps', { params: { ids } })
}

/**
 * 添加员工 POST /emps
 */
export function addEmp(data) {
  return request.post('/emps', data)
}

/**
 * 根据ID查询员工 GET /emps/{id}
 */
export function getEmpById(id) {
  return request.get(`/emps/${id}`)
}

/**
 * 修改员工 PUT /emps
 */
export function updateEmp(data) {
  return request.put('/emps', data)
}

/**
 * 查询全部员工 GET /emps/list
 */
export function getAllEmps() {
  return request.get('/emps/list')
}
