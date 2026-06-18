<script setup>
import { onMounted, ref } from 'vue'
import { getEmpGenderData, getEmpJobData } from '@/api/report'

const genderData = ref([])
const jobData = ref({ jobList: [], dataList: [] })
const genderTotal = ref(0)

const loadData = async () => {
  try {
    const [gRes, jRes] = await Promise.all([getEmpGenderData(), getEmpJobData()])
    if (gRes?.data?.code === 1) { genderData.value = gRes.data.data; genderTotal.value = gRes.data.data.reduce((a, b) => a + b.value, 0) }
    if (jRes?.data?.code === 1) jobData.value = jRes.data.data
  } catch (_) {}
}

onMounted(loadData)
</script>

<template>
  <div class="report-emp-page">
    <h2 class="page-title">员工信息统计</h2>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <template #header><span class="card-title">员工性别比例</span></template>
          <div class="gender-bars">
            <div v-for="item in genderData" :key="item.name" class="bar-row">
              <span class="bar-label">{{ item.name }}</span>
              <div class="bar-track"><div class="bar-fill" :style="{ width: genderTotal ? (item.value / genderTotal * 100) + '%' : '0%' }" /></div>
              <span class="bar-value">{{ item.value }}人（{{ genderTotal ? ((item.value / genderTotal * 100).toFixed(1)) : '0' }}%）</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <template #header><span class="card-title">各职位人数</span></template>
          <div class="job-chart" v-if="jobData.jobList?.length">
            <div v-for="(job, i) in jobData.jobList" :key="job" class="bar-row">
              <span class="bar-label">{{ job }}</span>
              <div class="bar-track"><div class="bar-fill bar-fill-job" :style="{ width: Math.min((jobData.dataList[i] / Math.max(...jobData.dataList) * 100), 100) + '%' }" /></div>
              <span class="bar-value">{{ jobData.dataList[i] }}人</span>
            </div>
          </div>
          <el-empty v-else description="暂无数据" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.report-emp-page { min-height:100%; padding:24px; box-sizing:border-box; background:radial-gradient(circle at top left,rgba(0,170,160,.08),transparent 30%),radial-gradient(circle at top right,rgba(0,127,164,.08),transparent 28%),linear-gradient(180deg,#f7fbff 0%,#eef5fb 100%); }
.page-title { margin:0 0 20px; font-size:24px; font-weight:800; color:#0f172a; }
.chart-card { border:none; border-radius:20px; box-shadow:0 14px 40px rgba(15,23,42,.08); height:100%; }
.card-title { font-weight:700; color:#334155; font-size:16px; }
.gender-bars,.job-chart { padding:8px 4px; }
.bar-row { display:flex; align-items:center; margin-bottom:16px; }
.bar-label { width:100px; font-size:14px; color:#475569; flex-shrink:0; }
.bar-track { flex:1; height:26px; background:#e9eaeb; border-radius:14px; overflow:hidden; }
.bar-fill { height:100%; border-radius:14px; background:linear-gradient(90deg,#0f7495,#00aaa0); transition:width 0.8s ease; }
.bar-fill-job { background:linear-gradient(90deg,#0ea5e9,#06b6d4); }
.bar-value { width:100px; text-align:right; font-size:13px; color:#64748b; flex-shrink:0; }
@media (max-width:900px) { .report-emp-page{padding:16px} .bar-label{width:70px} .bar-value{width:80px} }
</style>