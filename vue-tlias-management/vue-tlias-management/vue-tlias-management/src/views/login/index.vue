<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { login } from '@/api/login'

const router = useRouter()
const loginForm = ref({ username: '', password: '' })
const loading = ref(false)

const handleLogin = async () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    ElMessageBox.alert('请输入用户名和密码', '提示', { type: 'warning', confirmButtonText: '确定' })
    return
  }

  loading.value = true
  try {
    const response = await login(loginForm.value)
    const data = response?.data

    if (data?.code === 1 && data?.data?.token) {
      localStorage.setItem('token', data.data.token)
      localStorage.setItem('userInfo', JSON.stringify(data.data))
      ElMessage.success(`登录成功，欢迎 ${data.data.name}`)
      router.push('/index')
      return
    }

    ElMessage.error(data?.msg || '用户名或密码错误')
  } catch (error) {
    const status = error?.response?.status
    if (status === 401) {
      ElMessage.error('用户名或密码错误')
    } else {
      ElMessage.error('无法连接后端服务，请确认本地 8080 后端已启动')
      console.error('登录请求异常：', error)
    }
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  loginForm.value.username = ''
  loginForm.value.password = ''
}
</script>

<template>
  <div id="container">
    <div class="login-shell">
      <section class="brand-panel">
        <div class="brand-badge">Tlias</div>
        <h1 class="brand-title">智能辅助人员管理系统</h1>
        <p class="brand-desc">统一管理班级、学员、员工与统计信息，提供更清晰的教学数据协同体验。</p>

        <div class="brand-cards">
          <div class="brand-card">
            <el-icon><Notebook /></el-icon>
            <span>班级学员管理</span>
          </div>
          <div class="brand-card">
            <el-icon><Grid /></el-icon>
            <span>系统信息管理</span>
          </div>
          <div class="brand-card">
            <el-icon><DataLine /></el-icon>
            <span>数据统计分析</span>
          </div>
        </div>
      </section>

      <section class="login-panel">
        <div class="login-card">
          <div class="login-header">
            <p class="title">欢迎登录</p>
            <p class="subtitle">请输入账号信息以进入管理后台</p>
          </div>

          <el-form label-position="top" class="login-form" @submit.prevent="handleLogin">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="loginForm.username" placeholder="请输入用户名" size="large">
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item label="密码" prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                show-password
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item class="action-row">
              <el-button class="button primary-button" native-type="submit" type="primary" :loading="loading" :disabled="loading">
                {{ loading ? '登录中...' : '登录系统' }}
              </el-button>
              <el-button class="button ghost-button" @click="handleReset" :disabled="loading">
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped>
#container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px;
  background:
    linear-gradient(135deg, rgba(8, 30, 51, 0.72), rgba(10, 55, 89, 0.5)),
    url('../../assets/login.png') center / cover no-repeat;
  box-sizing: border-box;
}

.login-shell {
  width: min(1120px, 100%);
  min-height: 620px;
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  border-radius: 28px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(18px);
  box-shadow: 0 30px 80px rgba(7, 19, 34, 0.35);
  border: 1px solid rgba(255, 255, 255, 0.14);
}

.brand-panel {
  position: relative;
  padding: 56px 52px;
  color: #fff;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background: linear-gradient(160deg, rgba(0, 84, 125, 0.82), rgba(0, 127, 164, 0.62));
}

.brand-panel::before {
  content: '';
  position: absolute;
  inset: 24px;
  border-radius: 24px;
  border: 1px solid rgba(255, 255, 255, 0.12);
  pointer-events: none;
}

.brand-badge {
  width: fit-content;
  padding: 10px 16px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.14);
  font-size: 14px;
  letter-spacing: 1.5px;
  margin-bottom: 18px;
}

.brand-title {
  font-size: 46px;
  line-height: 1.15;
  margin: 0;
  font-weight: 800;
  letter-spacing: 1px;
}

.brand-desc {
  margin-top: 18px;
  max-width: 440px;
  font-size: 16px;
  line-height: 1.9;
  color: rgba(255, 255, 255, 0.88);
}

.brand-cards {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 14px;
  margin-top: 34px;
}

.brand-card {
  min-height: 104px;
  padding: 18px 16px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.12);
  border: 1px solid rgba(255, 255, 255, 0.16);
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-between;
  gap: 14px;
  backdrop-filter: blur(8px);
}

.brand-card :deep(.el-icon) {
  font-size: 22px;
  color: #d7fbff;
}

.brand-card span {
  font-size: 14px;
  font-weight: 600;
  color: #f2fbff;
}

.login-panel {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 36px;
  background: rgba(255, 255, 255, 0.92);
}

.login-card {
  width: min(420px, 100%);
}

.login-header {
  margin-bottom: 28px;
}

.title {
  font-size: 32px;
  color: #0f172a;
  font-weight: 800;
  margin: 0;
  letter-spacing: 0.5px;
}

.subtitle {
  margin-top: 8px;
  color: #64748b;
  font-size: 14px;
}

.login-form :deep(.el-form-item__label) {
  color: #334155;
  font-weight: 600;
  padding-bottom: 8px;
}

.login-form :deep(.el-input__wrapper) {
  border-radius: 14px;
  box-shadow: 0 0 0 1px rgba(148, 163, 184, 0.22) inset;
}

.login-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #0ea5e9 inset, 0 0 0 4px rgba(14, 165, 233, 0.12);
}

.button {
  width: 100%;
  height: 46px;
  border-radius: 14px;
  font-size: 15px;
  font-weight: 600;
}

.action-row {
  margin-top: 16px;
}

.action-row :deep(.el-form-item__content) {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px;
  width: 100%;
}

.primary-button {
  background: linear-gradient(135deg, #0f7495, #00aaa0);
  border: none;
  box-shadow: 0 12px 24px rgba(0, 127, 164, 0.24);
}

.ghost-button {
  border: 1px solid rgba(15, 116, 149, 0.24);
  color: #0f7495;
  background: rgba(255, 255, 255, 0.92);
}

.primary-button:hover,
.ghost-button:hover {
  transform: translateY(-1px);
}

@media (max-width: 960px) {
  #container {
    padding: 18px;
  }

  .login-shell {
    grid-template-columns: 1fr;
  }

  .brand-panel {
    padding: 34px 24px;
  }

  .brand-title {
    font-size: 34px;
  }

  .brand-cards {
    grid-template-columns: 1fr;
  }

  .login-panel {
    padding: 28px 20px 32px;
  }
}

@media (max-width: 560px) {
  .brand-panel::before {
    inset: 14px;
  }

  .title {
    font-size: 26px;
  }

  .action-row :deep(.el-form-item__content) {
    grid-template-columns: 1fr;
  }
}
</style>