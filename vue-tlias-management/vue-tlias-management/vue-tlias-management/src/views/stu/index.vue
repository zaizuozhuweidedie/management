<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getStudentList, addStudent, updateStudent, deleteStudents, getStudentById, studentViolation } from '@/api/student'
import { getAllClazzs } from '@/api/clazz'

/* ---------- 表格 ---------- */
const studentList = ref([])
const total = ref(0)
const loading = ref(false)

const searchForm = reactive({ name: '', degree: '', clazzId: '' })
const pagination = reactive({ page: 1, pageSize: 10 })

const degreeMap = { 1: '初中', 2: '高中', 3: '大专', 4: '本科', 5: '硕士', 6: '博士' }
const genderMap = { 1: '男', 2: '女' }

/* ---------- 班级下拉 ---------- */
const clazzOptions = ref([])
const loadClazzs = async () => {
  try { const res = await getAllClazzs(); if (res?.data?.code === 1) clazzOptions.value = res.data.data } catch (_) {}
}

/* ---------- 列表 ---------- */
const loadStudents = async () => {
  loading.value = true
  try {
    const params = { page: pagination.page, pageSize: pagination.pageSize, ...searchForm }
    Object.keys(params).forEach(k => { if (params[k] === '' || params[k] == null) delete params[k] })
    const res = await getStudentList(params)
    if (res?.data?.code === 1) { studentList.value = res.data.data.rows; total.value = res.data.data.total }
  } catch (_) { studentList.value = [] } finally { loading.value = false }
}

const handlePageChange = (p) => { pagination.page = p; loadStudents() }
const handleSizeChange = (s) => { pagination.pageSize = s; pagination.page = 1; loadStudents() }

/* ---------- 弹窗 ---------- */
const dialogVisible = ref(false)
const dialogTitle = ref('新增学员')
const isEdit = ref(false)
const saving = ref(false)
const formRef = ref()
const form = reactive({
  id: '', name: '', no: '', gender: 1, phone: '', degree: '', clazzId: '',
  idCard: '', isCollege: '', address: '', graduationDate: '',
  violationCount: 0, violationScore: 0
})

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  no: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  degree: [{ required: true, message: '请选择学历', trigger: 'change' }],
  clazzId: [{ required: true, message: '请选择班级', trigger: 'change' }]
}

const resetForm = () => {
  Object.assign(form, { id: '', name: '', no: '', gender: 1, phone: '', degree: '', clazzId: '', idCard: '', isCollege: '', address: '', graduationDate: '', violationCount: 0, violationScore: 0 })
  formRef.value?.clearValidate()
}

const openAddDialog = () => { isEdit.value = false; dialogTitle.value = '新增学员'; resetForm(); dialogVisible.value = true }
const openEditDialog = (row) => {
  isEdit.value = true; dialogTitle.value = '编辑学员'
  Object.assign(form, { id: row.id, name: row.name, no: row.no, gender: row.gender, phone: row.phone, degree: row.degree, clazzId: row.clazzId, idCard: row.idCard || '', isCollege: row.isCollege ?? '', address: row.address || '', graduationDate: row.graduationDate || '', violationCount: row.violationCount ?? 0, violationScore: row.violationScore ?? 0 })
  dialogVisible.value = true
}

const handleSave = async () => {
  await formRef.value?.validate()
  saving.value = true
  try {
    const payload = { ...form }
    if (payload.id === '') delete payload.id
    if (payload.isCollege === '') delete payload.isCollege
    if (payload.idCard === '') delete payload.idCard
    if (payload.address === '') delete payload.address
    if (payload.graduationDate === '') delete payload.graduationDate
    if (isEdit.value) { await updateStudent(payload); ElMessage.success('更新成功') }
    else { await addStudent(payload); ElMessage.success('新增成功') }
    dialogVisible.value = false; resetForm(); loadStudents()
  } catch (e) { ElMessage.error(e?.response?.data?.msg || '操作失败') } finally { saving.value = false }
}

/* ---------- 违纪处理 ---------- */
const violationDialogVisible = ref(false)
const violationStudent = ref({})
const violationScore = ref(0)
const violationSaving = ref(false)

const handleViolation = (row) => {
  violationStudent.value = row
  violationScore.value = 0
  violationDialogVisible.value = true
}

const submitViolation = async () => {
  if (!violationScore.value || violationScore.value <= 0) return ElMessage.warning('请输入正确的扣分数')
  violationSaving.value = true
  try {
    await studentViolation(violationStudent.value.id, violationScore.value)
    ElMessage.success('违纪处理成功')
    violationDialogVisible.value = false; loadStudents()
  } catch (e) { ElMessage.error(e?.response?.data?.msg || '违纪处理失败') } finally { violationSaving.value = false }
}

/* ---------- 删除 ---------- */
const selectedIds = ref([])
const handleSelectionChange = (rows) => { selectedIds.value = rows.map(r => r.id) }

const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除「${row.name}」吗？`, '删除学员', { type: 'warning' })
    .then(async () => { await deleteStudents(row.id); ElMessage.success('删除成功'); loadStudents() }).catch(() => {})
}

const handleBatchDelete = () => {
  if (!selectedIds.value.length) return ElMessage.warning('请先勾选学员')
  ElMessageBox.confirm(`确认删除选中的 ${selectedIds.value.length} 名学员吗？`, '批量删除', { type: 'warning' })
    .then(async () => { await deleteStudents(selectedIds.value.join(',')); ElMessage.success('批量删除成功'); loadStudents() }).catch(() => {})
}

onMounted(() => { loadClazzs(); loadStudents() })
</script>

<template>
  <div class="stu-page">
    <div class="page-header">
      <h2 class="page-title">学员管理</h2>
      <div class="header-actions">
        <el-button type="danger" plain @click="handleBatchDelete" :disabled="!selectedIds.length">批量删除</el-button>
        <el-button type="primary" @click="openAddDialog"><el-icon><Plus /></el-icon>新增学员</el-button>
      </div>
    </div>

    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="姓名"><el-input v-model="searchForm.name" clearable /></el-form-item>
        <el-form-item label="学历"><el-select v-model="searchForm.degree" clearable style="width:110px"><el-option v-for="(v,k) in degreeMap" :key="k" :label="v" :value="Number(k)" /></el-select></el-form-item>
        <el-form-item label="班级"><el-select v-model="searchForm.clazzId" clearable style="width:160px"><el-option v-for="c in clazzOptions" :key="c.id" :label="c.name" :value="c.id" /></el-select></el-form-item>
        <el-form-item><el-button type="primary" @click="pagination.page=1;loadStudents()">查询</el-button><el-button @click="Object.assign(searchForm,{name:'',degree:'',clazzId:''});pagination.page=1;loadStudents()">重置</el-button></el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="studentList" v-loading="loading" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" />
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="姓名" min-width="90" />
        <el-table-column prop="no" label="学号" min-width="120" />
        <el-table-column prop="gender" label="性别" width="60"><template #default="scope">{{ genderMap[scope.row.gender] }}</template></el-table-column>
        <el-table-column prop="phone" label="手机号" min-width="120" />
        <el-table-column label="学历" width="70"><template #default="scope">{{ degreeMap[scope.row.degree] }}</template></el-table-column>
        <el-table-column prop="clazzName" label="班级" min-width="120" />
        <el-table-column prop="violationCount" label="违纪次数" width="80" />
        <el-table-column prop="violationScore" label="违纪扣分" width="80" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{row}">
            <el-button link type="primary" @click="openEditDialog(row)">编辑</el-button>
            <el-button link type="warning" @click="handleViolation(row)">违纪</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrap"><el-pagination background layout="total,sizes,prev,pager,next" :total="total" :page-size="pagination.pageSize" :current-page="pagination.page" :page-sizes="[5,10,20,50]" @current-change="handlePageChange" @size-change="handleSizeChange" /></div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" align-center @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="姓名" prop="name"><el-input v-model="form.name" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="学号" prop="no"><el-input v-model="form.no" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="性别" prop="gender"><el-select v-model="form.gender" style="width:100%"><el-option label="男" :value="1" /><el-option label="女" :value="2" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="手机号" prop="phone"><el-input v-model="form.phone" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="学历" prop="degree"><el-select v-model="form.degree" style="width:100%"><el-option v-for="(v,k) in degreeMap" :key="k" :label="v" :value="Number(k)" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="班级" prop="clazzId"><el-select v-model="form.clazzId" style="width:100%"><el-option v-for="c in clazzOptions" :key="c.id" :label="c.name" :value="c.id" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="身份证号"><el-input v-model="form.idCard" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="是否院校"><el-select v-model="form.isCollege" style="width:100%" clearable><el-option label="是" :value="1" /><el-option label="否" :value="0" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="联系地址"><el-input v-model="form.address" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="毕业时间"><el-date-picker v-model="form.graduationDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" :loading="saving" @click="handleSave">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="violationDialogVisible" title="违纪处理" width="380px" align-center>
      <el-form label-width="90px">
        <el-form-item label="学员"><span>{{ violationStudent.name }}（{{ violationStudent.no }}）</span></el-form-item>
        <el-form-item label="扣分"><el-input-number v-model="violationScore" :min="1" :max="100" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="violationDialogVisible=false">取消</el-button><el-button type="warning" :loading="violationSaving" @click="submitViolation">确认扣分</el-button></template>
    </el-dialog>
  </div>
</template>

<style scoped>
.stu-page { min-height:100%; padding:24px; box-sizing:border-box; background:radial-gradient(circle at top left,rgba(0,170,160,.08),transparent 30%),radial-gradient(circle at top right,rgba(0,127,164,.08),transparent 28%),linear-gradient(180deg,#f7fbff 0%,#eef5fb 100%); }
.page-header { display:flex; align-items:center; justify-content:space-between; margin-bottom:20px; }
.page-title { margin:0; font-size:24px; font-weight:800; color:#0f172a; }
.header-actions { display:flex; gap:10px; }
.search-card { border:none; border-radius:20px; box-shadow:0 8px 30px rgba(15,23,42,.06); margin-bottom:16px; }
.table-card { border:none; border-radius:20px; box-shadow:0 14px 40px rgba(15,23,42,.08); }
.pagination-wrap { display:flex; justify-content:flex-end; margin-top:16px; }
.table-card :deep(.el-table th.el-table__cell) { background:#f1f7fb; color:#334155; font-weight:700; }
@media (max-width:768px) { .stu-page{padding:16px} .page-header{flex-direction:column;align-items:flex-start;gap:12px} }
</style>