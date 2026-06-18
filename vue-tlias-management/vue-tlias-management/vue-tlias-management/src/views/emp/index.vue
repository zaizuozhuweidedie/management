<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getEmpList, addEmp, updateEmp, deleteEmps } from '@/api/emp'
import { getDeptList } from '@/api/dept'
import { uploadFile } from '@/api/upload'

/* ---------- 表格数据 ---------- */
const empList = ref([])
const total = ref(0)
const loading = ref(false)

const searchForm = reactive({ name: '', gender: '', begin: '', end: '' })

const pagination = reactive({ page: 1, pageSize: 10 })

/* ---------- 枚举映射 ---------- */
const genderMap = { 1: '男', 2: '女' }
const jobMap = { 1: '班主任', 2: '讲师', 3: '学工主管', 4: '教研主管', 5: '咨询师' }

/* ---------- 部门下拉 ---------- */
const deptOptions = ref([])
const loadDepartments = async () => {
  try {
    const res = await getDeptList()
    if (res?.data?.code === 1) deptOptions.value = res.data.data
  } catch (_) {}
}

/* ---------- 列表加载 ---------- */
const loadEmpList = async () => {
  loading.value = true
  try {
    const params = { page: pagination.page, pageSize: pagination.pageSize, ...searchForm }
    Object.keys(params).forEach(k => { if (params[k] === '' || params[k] == null) delete params[k] })
    const res = await getEmpList(params)
    if (res?.data?.code === 1) {
      empList.value = res.data.data.rows
      total.value = res.data.data.total
    }
  } catch (_) {
    empList.value = []
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page) => { pagination.page = page; loadEmpList() }
const handleSizeChange = (size) => { pagination.pageSize = size; pagination.page = 1; loadEmpList() }

/* ---------- 新增 / 编辑 弹窗 ---------- */
const dialogVisible = ref(false)
const dialogTitle = ref('新增员工')
const isEdit = ref(false)
const saving = ref(false)
const empFormRef = ref()
const empForm = reactive({ id: '', username: '', name: '', password: '', gender: 1, image: '', phone: '', job: '', salary: '', entryDate: '', deptId: '' })

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }, { len: 11, message: '手机号必须为11位', trigger: 'blur' }]
}

const resetForm = () => {
  Object.assign(empForm, { id: '', username: '', name: '', password: '', gender: 1, image: '', phone: '', job: '', salary: '', entryDate: '', deptId: '' })
  previewImage.value = ''
  empFormRef.value?.clearValidate()
}

/* ---------- 头像上传 ---------- */
const uploading = ref(false)
const previewImage = ref('')
const fileInput = ref()

const handleFileChange = async (file) => {
  uploading.value = true
  try {
    const res = await uploadFile(file.raw)
    const data = res?.data
    if (data?.code === 1) {
      empForm.image = data.data
      previewImage.value = data.data
      ElMessage.success('头像上传成功')
    } else {
      ElMessage.error(data?.msg || '上传失败')
    }
  } catch (_) {
    ElMessage.error('头像上传失败')
  } finally {
    uploading.value = false
  }
}

const handlePreviewClick = () => {
  if (!fileInput.value) return
  const input = fileInput.value.$el?.querySelector('input[type="file"]') || fileInput.value
  if (input && typeof input.click === 'function') input.click()
}

const openAddDialog = () => { isEdit.value = false; dialogTitle.value = '新增员工'; resetForm(); dialogVisible.value = true }
const openEditDialog = (row) => {
  isEdit.value = true; dialogTitle.value = '编辑员工'
  Object.assign(empForm, { id: row.id, username: row.username, name: row.name, password: '', gender: row.gender, image: row.image || '', phone: row.phone || '', job: row.job, salary: row.salary, entryDate: row.entryDate, deptId: row.deptId })
  previewImage.value = row.image || ''
  dialogVisible.value = true
}

const handleSave = async () => {
  await empFormRef.value?.validate()
  saving.value = true
  try {
    const payload = { ...empForm }
    if (!payload.password) delete payload.password
    if (!payload.id) delete payload.id
    if (payload.salary === '' || payload.salary == null) delete payload.salary
    if (payload.deptId === '' || payload.deptId == null) delete payload.deptId
    if (payload.job === '' || payload.job == null) delete payload.job
    if (payload.entryDate === '' || payload.entryDate == null) delete payload.entryDate
    if (payload.image === '' || payload.image == null) delete payload.image
    if (isEdit.value) { await updateEmp(payload); ElMessage.success('更新成功') }
    else { await addEmp(payload); ElMessage.success('新增成功') }
    dialogVisible.value = false; resetForm(); loadEmpList()
  } catch (e) {
    ElMessage.error(e?.response?.data?.msg || '操作失败')
  } finally { saving.value = false }
}

/* ---------- 删除 ---------- */
const selectedIds = ref([])
const handleSelectionChange = (rows) => { selectedIds.value = rows.map(r => r.id) }

const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除「${row.name}」吗？`, '删除员工', { type: 'warning' })
    .then(async () => { await deleteEmps(row.id); ElMessage.success('删除成功'); loadEmpList() })
    .catch(() => {})
}

const handleBatchDelete = () => {
  if (!selectedIds.value.length) return ElMessage.warning('请先勾选员工')
  ElMessageBox.confirm(`确认删除选中的 ${selectedIds.value.length} 名员工吗？`, '批量删除', { type: 'warning' })
    .then(async () => { await deleteEmps(selectedIds.value.join(',')); ElMessage.success('批量删除成功'); loadEmpList() })
    .catch(() => {})
}

onMounted(() => { loadDepartments(); loadEmpList() })
</script>

<template>
  <div class="emp-page">
    <div class="page-header">
      <h2 class="page-title">员工管理</h2>
      <div class="header-actions">
        <el-button type="danger" plain @click="handleBatchDelete" :disabled="!selectedIds.length">批量删除</el-button>
        <el-button type="primary" @click="openAddDialog"><el-icon><Plus /></el-icon>新增员工</el-button>
      </div>
    </div>

    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="姓名"><el-input v-model="searchForm.name" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="性别">
          <el-select v-model="searchForm.gender" placeholder="全部" clearable style="width:100px">
            <el-option label="男" :value="1" /><el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="入职日期">
          <el-date-picker v-model="searchForm.begin" type="date" placeholder="开始" value-format="YYYY-MM-DD" style="width:140px" />
        </el-form-item>
        <el-form-item label="至">
          <el-date-picker v-model="searchForm.end" type="date" placeholder="结束" value-format="YYYY-MM-DD" style="width:140px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="pagination.page=1;loadEmpList()">查询</el-button>
          <el-button @click="Object.assign(searchForm,{name:'',gender:'',begin:'',end:''});pagination.page=1;loadEmpList()">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="empList" v-loading="loading" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" />
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="姓名" min-width="100" />
        <el-table-column prop="gender" label="性别" width="70">
          <template #default="{row}">{{ genderMap[row.gender] }}</template>
        </el-table-column>
        <el-table-column prop="image" label="头像" width="80">
          <template #default="{row}"><el-avatar v-if="row.image" :src="row.image" size="small" /></template>
        </el-table-column>
        <el-table-column prop="job" label="职位" width="100">
          <template #default="{row}">{{ jobMap[row.job] || row.job }}</template>
        </el-table-column>
        <el-table-column prop="salary" label="薪资" width="100" />
        <el-table-column prop="entryDate" label="入职日期" min-width="120" />
        <el-table-column prop="deptName" label="部门" min-width="120" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{row}">
            <el-button link type="primary" @click="openEditDialog(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrap">
        <el-pagination background layout="total,sizes,prev,pager,next" :total="total" :page-size="pagination.pageSize" :current-page="pagination.page" :page-sizes="[5,10,20,50]" @current-change="handlePageChange" @size-change="handleSizeChange" />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" align-center @close="resetForm">
      <el-form ref="empFormRef" :model="empForm" :rules="rules" label-width="90px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="用户名" prop="username"><el-input v-model="empForm.username" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="姓名" prop="name"><el-input v-model="empForm.name" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">            <el-col :span="12"><el-form-item label="手机号" prop="phone"><el-input v-model="empForm.phone" maxlength="11" /></el-form-item></el-col>
          </el-row>
          <el-row :gutter="16">          <el-col :span="12"><el-form-item label="密码"><el-input v-model="empForm.password" placeholder="留空则不修改" show-password /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="性别" prop="gender">
            <el-select v-model="empForm.gender" style="width:100%"><el-option label="男" :value="1" /><el-option label="女" :value="2" /></el-select>
          </el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="职位">
            <el-select v-model="empForm.job" style="width:100%" clearable>
              <el-option v-for="(v,k) in jobMap" :key="k" :label="v" :value="Number(k)" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="薪资"><el-input-number v-model="empForm.salary" :min="0" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="入职日期"><el-date-picker v-model="empForm.entryDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="部门">
            <el-select v-model="empForm.deptId" style="width:100%" clearable>
              <el-option v-for="d in deptOptions" :key="d.id" :label="d.name" :value="d.id" />
            </el-select>
          </el-form-item></el-col>
        </el-row>
        <el-form-item label="头像">
          <div class="avatar-upload">
            <el-upload
              ref="fileInput"
              :show-file-list="false"
              :before-upload="() => false"
              accept="image/*"
              @change="handleFileChange"
            >
              <el-button :loading="uploading" size="small" type="primary" plain>
                <el-icon><Upload /></el-icon> 选择图片
              </el-button>
            </el-upload>
            <span style="margin: 0 8px; color: #94a3b8; font-size: 12px">或</span>
            <el-input v-model="empForm.image" placeholder="输入图片URL" style="flex: 1" size="small" />
          </div>
          <div v-if="previewImage" class="avatar-preview">
            <el-avatar :src="previewImage" :size="80" shape="square" />
            <span class="preview-url">{{ previewImage }}</span>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.emp-page {
  min-height: 100%;
  padding: 24px;
  box-sizing: border-box;
  background:
    radial-gradient(circle at top left, rgba(0, 170, 160, 0.08), transparent 30%),
    radial-gradient(circle at top right, rgba(0, 127, 164, 0.08), transparent 28%),
    linear-gradient(180deg, #f7fbff 0%, #eef5fb 100%);
}
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}
.page-title {
  margin: 0;
  font-size: 24px;
  font-weight: 800;
  color: #0f172a;
}
.header-actions {
  display: flex;
  gap: 10px;
}
.search-card {
  border: none;
  border-radius: 20px;
  box-shadow: 0 8px 30px rgba(15, 23, 42, 0.06);
  margin-bottom: 16px;
}
.table-card {
  border: none;
  border-radius: 20px;
  box-shadow: 0 14px 40px rgba(15, 23, 42, 0.08);
}
.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
.table-card :deep(.el-table th.el-table__cell) {
  background: #f1f7fb;
  color: #334155;
  font-weight: 700;
}
@media (max-width: 768px) {
  .emp-page { padding: 16px; }
  .page-header { flex-direction: column; align-items: flex-start; gap: 12px; }
}

.avatar-upload {
  display: flex;
  align-items: center;
}
.avatar-preview {
  margin-top: 12px;
  display: flex;
  align-items: center;
  gap: 12px;
}
.preview-url {
  font-size: 12px;
  color: #64748b;
  word-break: break-all;
}
</style>