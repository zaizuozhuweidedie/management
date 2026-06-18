<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDeptList, addDept, updateDept, deleteDept } from '@/api/dept'

const deptList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增部门')
const saving = ref(false)
const isEdit = ref(false)

const deptFormRef = ref()
const deptForm = reactive({
  id: '',
  name: ''
})

const rules = {
  name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }]
}

const loadDeptList = async () => {
  loading.value = true
  try {
    const response = await getDeptList()
    const data = response?.data

    if (data && data.code === 1 && Array.isArray(data.data)) {
      deptList.value = data.data
    } else if (Array.isArray(data)) {
      deptList.value = data
    } else {
      deptList.value = []
      ElMessage.warning('部门列表接口返回数据格式不符合预期')
    }
  } catch (error) {
    deptList.value = []
    console.error('部门列表请求失败：', error)
    ElMessage.error('部门列表请求失败，请检查本地 8080 后端和 /api 代理配置')
  } finally {
    loading.value = false
  }
}

const formatTime = (value) => {
  if (!value) return '-'
  return String(value).replace('T', ' ')
}

const resetForm = () => {
  deptForm.id = ''
  deptForm.name = ''
  deptFormRef.value?.clearValidate()
}

const openAddDialog = () => {
  isEdit.value = false
  dialogTitle.value = '新增部门'
  resetForm()
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑部门'
  deptForm.id = row.id
  deptForm.name = row.name
  dialogVisible.value = true
}

const handleSave = async () => {
  await deptFormRef.value?.validate()

  saving.value = true
  try {
    if (isEdit.value) {
      await updateDept({ id: deptForm.id, name: deptForm.name })
      ElMessage.success('部门信息已更新')
    } else {
      await addDept(deptForm.name)
      ElMessage.success('部门已新增')
    }

    dialogVisible.value = false
    resetForm()
    await loadDeptList()
  } catch (error) {
    const msg = error?.response?.data?.msg || (isEdit.value ? '修改部门失败' : '新增部门失败')
    ElMessage.error(msg)
  } finally {
    saving.value = false
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除部门「${row.name}」吗？`, '删除部门', {
    confirmButtonText: '确认删除',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        const response = await deleteDept(row.id)

        const data = response?.data
        if (data?.code === 1) {
          ElMessage.success('部门已删除')
          await loadDeptList()
          return
        }

        ElMessage.error(data?.msg || '删除部门失败')
      } catch (error) {
        console.error('删除部门请求失败：', error)
        ElMessage.error('删除部门请求失败，请检查本地 8080 后端和 /api 代理配置')
      }
    })
    .catch(() => {})
}

const handleDialogClose = () => {
  resetForm()
}

onMounted(() => {
  loadDeptList()
})
</script>

<template>
  <div class="dept-page">
    <div class="page-header">
      <div>
        <h2 class="page-title">部门管理</h2>
        <p class="page-desc">支持部门列表查询、新增、编辑和删除操作。</p>
      </div>

      <el-button type="primary" class="add-button" @click="openAddDialog">
        <el-icon><Plus /></el-icon>
        新增部门
      </el-button>
    </div>

    <el-card class="table-card" shadow="never">
      <el-table :data="deptList" v-loading="loading" stripe border style="width: 100%">
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="name" label="部门名称" min-width="180" />
        <el-table-column prop="createTime" label="创建时间" min-width="200">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="修改时间" min-width="200">
          <template #default="scope">
            {{ formatTime(scope.row.updateTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="scope">
            <el-button link type="primary" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      align-center
      @close="handleDialogClose"
    >
      <el-form ref="deptFormRef" :model="deptForm" :rules="rules" label-width="90px">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="deptForm.name" placeholder="请输入部门名称" maxlength="20" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.dept-page {
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
  gap: 16px;
  margin-bottom: 20px;
}

.page-title {
  margin: 0;
  font-size: 24px;
  font-weight: 800;
  color: #0f172a;
}

.page-desc {
  margin: 8px 0 0;
  color: #64748b;
  font-size: 14px;
}

.add-button {
  height: 42px;
  border-radius: 12px;
  padding: 0 18px;
  box-shadow: 0 12px 24px rgba(0, 127, 164, 0.16);
}

.table-card {
  border: none;
  border-radius: 20px;
  box-shadow: 0 14px 40px rgba(15, 23, 42, 0.08);
}

.table-card :deep(.el-card__body) {
  padding: 18px;
}

.table-card :deep(.el-table) {
  border-radius: 14px;
  overflow: hidden;
}

.table-card :deep(.el-table th.el-table__cell) {
  background: #f1f7fb;
  color: #334155;
  font-weight: 700;
}

.table-card :deep(.el-button.is-link) {
  font-weight: 600;
}

@media (max-width: 768px) {
  .dept-page {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
