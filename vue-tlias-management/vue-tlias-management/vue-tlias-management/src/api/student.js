import request from '@/utils/request'

/** 学员列表查询（条件分页） GET /students */
export function getStudentList(params) {
  return request.get('/students', { params })
}

/** 批量删除学员 DELETE /students/{ids} */
export function deleteStudents(ids) {
  return request.delete(`/students/${ids}`)
}

/** 添加学员 POST /students */
export function addStudent(data) {
  return request.post('/students', data)
}

/** 根据ID查询学员 GET /students/{id} */
export function getStudentById(id) {
  return request.get(`/students/${id}`)
}

/** 修改学员 PUT /students */
export function updateStudent(data) {
  return request.put('/students', data)
}

/** 违纪处理 PUT /students/violation/{id}/{score} */
export function studentViolation(id, score) {
  return request.put(`/students/violation/${id}/${score}`)
}
