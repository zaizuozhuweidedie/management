<script setup>
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
import { useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<template>
  <div class="common-layout">
    <el-container class="layout-shell">
      <!-- Header 区域 -->
      <el-header class="header">
        <span class="title">智能辅助人员管理系统</span>
        <span class="right_tool">
          <a href="">
            <el-icon><EditPen /></el-icon> 修改密码 &nbsp;&nbsp;&nbsp; |  &nbsp;&nbsp;&nbsp;
          </a>
          <a href="javascript:;" @click="handleLogout">
            <el-icon><SwitchButton /></el-icon> 退出登录
          </a>
        </span>
      </el-header>
      
      <el-container>
        <!-- 左侧菜单 -->
        <el-aside width="240px" class="aside">
          <div class="aside-head">
            <div class="aside-title">导航菜单</div>
            <div class="aside-subtitle">快速进入各管理模块</div>
          </div>
          <el-scrollbar class="menu-scrollbar">
            <el-menu
              router
              :default-active="route.path"
              class="menu"
              :unique-opened="true"
            >
              <el-menu-item index="/index" class="menu-home">
                <el-icon><House /></el-icon>
                <span>首页</span>
              </el-menu-item>

              <el-sub-menu index="1">
                <template #title>
                  <el-icon><Menu /></el-icon>
                  <span>班级学员管理</span>
                </template>
                <el-menu-item index="/clazz">
                  <el-icon class="menu-icon"><School /></el-icon>
                  <span>班级管理</span>
                </el-menu-item>
                <el-menu-item index="/stu">
                  <el-icon class="menu-icon"><UserFilled /></el-icon>
                  <span>学员管理</span>
                </el-menu-item>
              </el-sub-menu>

              <el-sub-menu index="2">
                <template #title>
                  <el-icon><Setting /></el-icon>
                  <span>系统信息管理</span>
                </template>
                <el-menu-item index="/dept">
                  <el-icon class="menu-icon"><OfficeBuilding /></el-icon>
                  <span>部门管理</span>
                </el-menu-item>
                <el-menu-item index="/emp">
                  <el-icon class="menu-icon"><Avatar /></el-icon>
                  <span>员工管理</span>
                </el-menu-item>
              </el-sub-menu>

              <el-sub-menu index="3">
                <template #title>
                  <el-icon><Histogram /></el-icon>
                  <span>数据统计管理</span>
                </template>
                <el-menu-item index="/report/emp">
                  <el-icon class="menu-icon"><DataAnalysis /></el-icon>
                  <span>员工信息统计</span>
                </el-menu-item>
                <el-menu-item index="/report/stu">
                  <el-icon class="menu-icon"><PieChart /></el-icon>
                  <span>学员信息统计</span>
                </el-menu-item>
                <el-menu-item index="/log">
                  <el-icon class="menu-icon"><List /></el-icon>
                  <span>日志信息统计</span>
                </el-menu-item>
              </el-sub-menu>
            </el-menu>
          </el-scrollbar>
        </el-aside>
        
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
      
    </el-container>
  </div>
</template>

<style scoped>
.header {
  background: linear-gradient(135deg, #00547d 0%, #007fa4 28%, #00aaa0 58%, #00d072 100%);
  height: 60px;
  box-shadow: 0 10px 30px rgba(0, 84, 125, 0.22);
}

.title {
  color: white;
  font-size: 40px;
  font-family: 楷体;
  line-height: 60px;
  font-weight: bolder;
}

.right_tool{
  float: right;
  line-height: 60px;
}

a {
  color: white;
  text-decoration: none;
  transition: color 0.3s ease, text-shadow 0.3s ease;
}

a:hover {
  color: #f7ff9a;
  text-shadow: 0 0 10px rgba(247, 255, 154, 0.55);
}

.aside {
  width: 240px;
  border-right: 1px solid #d9e2ea;
  background: linear-gradient(180deg, #fbfdff 0%, #f4f8fc 100%);
  min-height: calc(100vh - 60px);
  padding: 14px 10px 10px;
  box-sizing: border-box;
}

.aside-head {
  margin: 4px 10px 12px;
  padding: 14px 14px 12px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.85);
  border: 1px solid rgba(0, 127, 164, 0.08);
  box-shadow: 0 10px 24px rgba(15, 23, 42, 0.05);
}

.aside-title {
  font-size: 16px;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: 0.5px;
}

.aside-subtitle {
  margin-top: 6px;
  font-size: 12px;
  color: #64748b;
}

.menu-scrollbar {
  height: calc(100vh - 140px);
}

.menu {
  border-right: none;
  background: transparent;
}

.menu :deep(.el-sub-menu__title),
.menu :deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
  border-radius: 14px;
  margin: 8px 10px;
}

.menu :deep(.el-sub-menu__title) {
  font-weight: 600;
  color: #334155;
  background: rgba(255, 255, 255, 0.6);
  transition: background-color 0.25s ease, color 0.25s ease, transform 0.25s ease;
}

.menu :deep(.el-menu-item) {
  color: #475569;
  transition: background-color 0.25s ease, color 0.25s ease, transform 0.25s ease,
    box-shadow 0.25s ease;
}

.menu :deep(.el-sub-menu__title:hover),
.menu :deep(.el-menu-item:hover) {
  background: rgba(0, 127, 164, 0.08);
  color: #0f7495;
  transform: translateX(2px);
}

.menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(90deg, #e3f8ff 0%, #d6f3fb 50%, #edf9ff 100%);
  color: #007fa4;
  font-weight: 600;
  box-shadow: 0 10px 20px rgba(0, 127, 164, 0.14);
}

.menu-home :deep(.el-icon) {
  font-size: 18px;
}

.menu-icon {
  margin-right: 8px;
  font-size: 16px;
  color: #0f7495;
  flex-shrink: 0;
}

.menu :deep(.el-menu-item span),
.menu :deep(.el-sub-menu__title span) {
  flex: 1;
}

.main {
  background: #f5f8fb;
  min-height: calc(100vh - 60px);
  height: calc(100vh - 60px);
  padding: 0;
}
</style>
