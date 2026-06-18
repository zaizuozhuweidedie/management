<script setup>
import { onMounted, ref } from 'vue'
import { getStudentDegreeData, getStudentCountData } from '@/api/report'

const degreeData = ref([])
const countData = ref({ clazzList: [], dataList: [] })
const degreeTotal = ref(0)

const loadData = async () => {
  try {
    const [dRes, cRes] = await Promise.all([getStudentDegreeData(), getStudentCountData()])
    if (dRes?.data?.code === 1) { degreeData.value = dRes.data.data; degreeTotal.value = dRes.data.data.reduce((a, b) => a + b.value, 0) }
    if (cRes?.data?.code === 1) countData.value = cRes.data.data
  } catch (_) {}
}

onMounted(loadData)
</script>

<template>
  <div class="report-stu-page">
    <h2 class="page-title">学员信息统计</h2>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <template #header><span class="card-title">学员学历分布</span></template>
          <div class="bar-chart">
            <div v-for="item in degreeData" :key="item.name" class="bar-row">
              <span class="bar-label">{{ item.name }}</span>
              <div class="bar-track"><div class="bar-fill" :style="{ width: degreeTotal ? (item.value / degreeTotal * 100) + '%' : '0%' }" /></div>
              <span class="bar-value">{{ item.value }}人</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <template #header><span class="card-title">班级人数统计</span></template>
          <div v-if="countData.clazzList?.length" class="bar-chart">
            <div v-for="(clazz, i) in countData.clazzList" :key="clazz" class="bar-row">
              <span class="bar-label bar-label-clazz">{{ clazz }}</span>
              <div class="bar-track"><div class="bar-fill bar-fill-clazz" :style="{ width: Math.min((countData.dataList[i] / Math.max(...countData.dataList) * 100), 100) + '%' }" /></div>
              <span class="bar-value">{{ countData.dataList[i] }}人</span>
            </div>
          </div>
          <el-empty v-else description="暂无数据" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.report-stu-page { min-height:100%; padding:24px; box-sizing:border-box; background:radial-gradient(circle at top left,rgba(0,170,160,.08),transparent 30%),radial-gradient(circle at top right,rgba(0,127,164,.08),transparent 28%),linear-gradient(180deg,#f7fbff 0%,#eef5fb 100%); }
.page-title { margin:0 0 20px; font-size:24px; font-weight:800; color:#0f172a; }
.chart-card { border:none; border-radius:20px; box-shadow:0 14px 40px rgba(15,23,42,.08); height:100%; }
.card-title { font-weight:700; color:#334155; font-size:16px; }
.bar-chart { padding:8px 4px; }
.bar-row { display:flex; align-items:center; margin-bottom:14px; gap:10px; }
.bar-label { width:100px; font-size:14px; color:#475569; flex-shrink:0; }
.bar-label-clazz { width:160px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
.bar-track { flex:1; height:26px; background:#e9eaeb; border-radius:14px; overflow:hidden; }
.bar-fill { height:100%; border-radius:14px; background:linear-gradient(90deg,#0f7495,#00aaa0); transition:width 0.8s ease; }
.bar-fill-clazz { background:linear-gradient(90deg,#6366f1,#8b5cf6); }
.bar-value { width:70px; text-align:right; font-size:13px; color:#64748b; flex-shrink:0; }
@media (max-width:900px) { .report-stu-page{padding:16px} .bar-label{width:70px} .bar-label-clazz{width:120px} }
</style>