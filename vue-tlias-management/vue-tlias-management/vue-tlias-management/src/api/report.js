import request from '@/utils/request'

/** 员工性别统计 GET /report/empGenderData */
export function getEmpGenderData() {
  return request.get('/report/empGenderData')
}

/** 员工职位人数统计 GET /report/empJobData */
export function getEmpJobData() {
  return request.get('/report/empJobData')
}

/** 学员学历统计 GET /report/studentDegreeData */
export function getStudentDegreeData() {
  return request.get('/report/studentDegreeData')
}

/** 班级人数统计 GET /report/studentCountData */
export function getStudentCountData() {
  return request.get('/report/studentCountData')
}

/** 日志列表查询 GET /log/page */
export function getLogPage(params) {
  return request.get('/log/page', { params })
}
