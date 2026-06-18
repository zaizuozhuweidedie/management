<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getClazzList, addClazz, updateClazz, deleteClazz } from '@/api/clazz'
import { getAllEmps } from '@/api/emp'

/* ---------- 表格数据 ---------- */
const clazzList = ref([])
const total = ref(0)
const loading = ref(false)

const searchForm = reactive({ name: '', begin: '', end: '' })

const pagination = reactive({ page: 1, pageSize: 10 })

/* ---------- 学科映射 ---------- */
const subjectMap = { 1: 'Java', 2: '前端', 3: '大数据', 4: 'Python', 5: 'Go', 6: '嵌入式' }

/* ---------- 员工下拉（班主任） ---------- */
const empOptions = ref([])
const loadEmps = async () => {
  try {
    const res = await getAllEmps()
    if (res?.data?.code === 1) empOptions.value = res.data.data
  } catch (_) {}
}

/* ---------- 列表加载 ---------- */
const loadClazzList = async () => {
  loading.value = true
  try {
    const params = { page: pagination.page, pageSize: pagination.pageSize, ...searchForm }
    Object.keys(params).forEach(k => { if (params[k] === '' || params[k] == null) delete params[k] })
    const res = await getClazzList(params)
    if (res?.data?.code === 1) {
      clazzList.value = res.data.data.rows
      total.value = res.data.data.total
    }
  } catch (_) {
    clazzList.value = []
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page) => { pagination.page = page; loadClazzList() }
const handleSizeChange = (size) => { pagination.pageSize = size; pagination.page = 1; loadClazzList() }

/* ---------- 新增 / 编辑 弹窗 ---------- */
const dialogVisible = ref(false)
const dialogTitle = ref('新增班级')
const isEdit = ref(false)
const saving = ref(false)
const clazzFormRef = ref()
const clazzForm = reactive({ id: '', name: '', room: '', beginDate: '', endDate: '', masterId: '', subject: '' })

const rules = {
  name: [{ required: true, message: '请输入班级名称', trigger: 'blur' }],
  room: [{ required: true, message: '请输入教室', trigger: 'blur' }],
  beginDate: [{ required: true, message: '请选择开课时间', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结课时间', trigger: 'change' }],
  subject: [{ required: true, message: '请选择学科', trigger: 'change' }]
}

const resetForm = () => {
  Object.assign(clazzForm, { id: '', name: '', room: '', beginDate: '', endDate: '', masterId: '', subject: '' })
  clazzFormRef.value?.clearValidate()
}

const openAddDialog = () => { isEdit.value = false; dialogTitle.value = '新增班级'; resetForm(); dialogVisible.value = true }
const openEditDialog = (row) => {
  isEdit.value = true; dialogTitle.value = '编辑班级'
  Object.assign(clazzForm, { id: row.id, name: row.name, room: row.room, beginDate: row.beginDate, endDate: row.endDate, masterId: row.masterId, subject: row.subject })
  dialogVisible.value = true
}

const handleSave = async () => {
  await clazzFormRef.value?.validate()
  saving.value = true
  try {
    const payload = { ...clazzForm }
    if (isEdit.value) { await updateClazz(payload); ElMessage.success('更新成功') }
    else { await addClazz(payload); ElMessage.success('新增成功') }
    dialogVisible.value = false; resetForm(); loadClazzList()
  } catch (e) {
    ElMessage.error(e?.response?.data?.msg || '操作失败')
  } finally { saving.value = false }
}

/* ---------- 删除 ---------- */
const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除班级「${row.name}」吗？`, '删除班级', { type: 'warning' })
    .then(async () => { await deleteClazz(row.id); ElMessage.success('删除成功'); loadClazzList() })
    .catch(() => {})
}

onMounted(() => { loadEmps(); loadClazzList() })
</script>

<template>
  <div class="clazz-page">
    <div class="page-header">
      <h2 class="page-title">班级管理</h2>
      <el-button type="primary" @click="openAddDialog"><el-icon><Plus /></el-icon>新增班级</el-button>
    </div>

    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="班级名称"><el-input v-model="searchForm.name" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="结课日期">
          <el-date-picker v-model="searchForm.begin" type="date" placeholder="开始" value-format="YYYY-MM-DD" style="width:140px" />
        </el-form-item>
        <el-form-item label="至">
          <el-date-picker v-model="searchForm.end" type="date" placeholder="结束" value-format="YYYY-MM-DD" style="width:140px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="pagination.page=1;loadClazzList()">查询</el-button>
          <el-button @click="Object.assign(searchForm,{name:'',begin:'',end:''});pagination.page=1;loadClazzList()">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="clazzList" v-loading="loading" stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="班级名称" min-width="140" />
        <el-table-column prop="room" label="教室" width="80" />
        <el-table-column prop="beginDate" label="开课时间" min-width="120" />
        <el-table-column prop="endDate" label="结课时间" min-width="120" />
        <el-table-column prop="masterName" label="班主任" min-width="100" />
        <el-table-column label="学科" width="90">
          <template #default="{row}">{{ subjectMap[row.subject] || row.subject }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90" />
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="550px" align-center @close="resetForm">
      <el-form ref="clazzFormRef" :model="clazzForm" :rules="rules" label-width="90px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="班级名称" prop="name"><el-input v-model="clazzForm.name" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="教室" prop="room"><el-input v-model="clazzForm.room" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="开课时间" prop="beginDate"><el-date-picker v-model="clazzForm.beginDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="结课时间" prop="endDate"><el-date-picker v-model="clazzForm.endDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="学科" prop="subject">
            <el-select v-model="clazzForm.subject" style="width:100%">
              <el-option v-for="(v,k) in subjectMap" :key="k" :label="v" :value="Number(k)" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="班主任">
            <el-select v-model="clazzForm.masterId" style="width:100%" clearable filterable>
              <el-option v-for="e in empOptions" :key="e.id" :label="e.name" :value="e.id" />
            </el-select>
          </el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.clazz-page {
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
  .clazz-page { padding: 16px; }
  .page-header { flex-direction: column; align-items: flex-start; gap: 12px; }
}
</style>