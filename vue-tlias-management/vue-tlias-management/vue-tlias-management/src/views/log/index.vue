<script setup>
import { onMounted, reactive, ref } from 'vue'
import { getLogPage } from '@/api/report'

const logList = ref([])
const total = ref(0)
const loading = ref(false)
const pagination = reactive({ page: 1, pageSize: 10 })

const loadLogs = async () => {
  loading.value = true
  try {
    const res = await getLogPage({ page: pagination.page, pageSize: pagination.pageSize })
    if (res?.data?.code === 1) { logList.value = res.data.data.rows; total.value = res.data.data.total }
  } catch (_) { logList.value = [] } finally { loading.value = false }
}

const handlePageChange = (p) => { pagination.page = p; loadLogs() }
const handleSizeChange = (s) => { pagination.pageSize = s; pagination.page = 1; loadLogs() }

onMounted(loadLogs)
</script>

<template>
  <div class="log-page">
    <h2 class="page-title">日志信息统计</h2>

    <el-card shadow="never" class="table-card">
      <el-table :data="logList" v-loading="loading" stripe border>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="operateEmpName" label="操作人" width="100" />
        <el-table-column prop="operateTime" label="操作时间" min-width="160">
          <template #default="{row}">{{ row.operateTime?.replace('T', ' ') }}</template>
        </el-table-column>
        <el-table-column prop="className" label="类名" min-width="200" show-overflow-tooltip />
        <el-table-column prop="methodName" label="方法名" width="100" />
        <el-table-column prop="costTime" label="耗时(ms)" width="90" />
        <el-table-column prop="methodParams" label="请求参数" min-width="200" show-overflow-tooltip />
        <el-table-column prop="returnValue" label="返回值" min-width="200" show-overflow-tooltip />
      </el-table>
      <div class="pagination-wrap">
        <el-pagination background layout="total,sizes,prev,pager,next" :total="total" :page-size="pagination.pageSize" :current-page="pagination.page" :page-sizes="[5,10,20,50]" @current-change="handlePageChange" @size-change="handleSizeChange" />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.log-page { min-height:100%; padding:24px; box-sizing:border-box; background:radial-gradient(circle at top left,rgba(0,170,160,.08),transparent 30%),radial-gradient(circle at top right,rgba(0,127,164,.08),transparent 28%),linear-gradient(180deg,#f7fbff 0%,#eef5fb 100%); }
.page-title { margin:0 0 20px; font-size:24px; font-weight:800; color:#0f172a; }
.table-card { border:none; border-radius:20px; box-shadow:0 14px 40px rgba(15,23,42,.08); }
.pagination-wrap { display:flex; justify-content:flex-end; margin-top:16px; }
.table-card :deep(.el-table th.el-table__cell) { background:#f1f7fb; color:#334155; font-weight:700; }
@media (max-width:768px) { .log-page{padding:16px} }
</style>